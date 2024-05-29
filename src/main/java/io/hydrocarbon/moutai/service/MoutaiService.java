package io.hydrocarbon.moutai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.hydrocarbon.moutai.constant.Constants;
import io.hydrocarbon.moutai.entity.moutai.*;
import io.hydrocarbon.moutai.enums.MoutaiUrl;
import io.hydrocarbon.moutai.param.Response;
import io.hydrocarbon.moutai.param.moutai.QueryMoutaiUserParam;
import io.hydrocarbon.moutai.param.moutai.request.*;
import io.hydrocarbon.moutai.param.moutai.response.LoginResponse;
import io.hydrocarbon.moutai.property.MoutaiProperty;
import io.hydrocarbon.moutai.repository.moutai.*;
import io.hydrocarbon.moutai.util.HttpUtil;
import io.hydrocarbon.moutai.util.MoutaiUtil;
import io.hydrocarbon.moutai.util.TimeUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MoutaiService {

    private final MoutaiProperty moutaiProperty;

    private final MoutaiUserRepository moutaiUserRepository;

    private final MoutaiShopRepository moutaiShopRepository;

    private final MoutaiItemRepository moutaiItemRepository;

    private final MoutaiAppointmentRepository moutaiAppointmentRepository;

    private final MoutaiConfigRepository moutaiConfigRepository;

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return 设备 ID
     */
    public String sendVerificationCode(String mobile) {
        Long timestamp = TimeUtil.nowTimestamp();

        SendCodeRequest requestBody = SendCodeRequest.builder()
                .mobile(mobile)
                .md5(signature(mobile, timestamp))
                .timestamp(timestamp)
                .build();

        String deviceId = MoutaiUtil.getDeviceId();

        Response<Void> response = HttpUtil.request(
                MoutaiUrl.SEND_CODE_URL,
                new TypeReference<>() {
                },
                MoutaiUtil.getDefaultHeaderMap(deviceId, moutaiProperty.getAppVersion()),
                requestBody);

        if (Objects.isNull(response) || !Integer.valueOf(2000).equals(response.getCode())) {
            log.error("sendVerificationCode failed: {}", response);
            return null;
        } else {
            log.info("sendVerificationCode success");
            return deviceId;
        }
    }

    /**
     * 保存用户信息
     *
     * @param body 请求体
     * @return 是否保存成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveMoutaiUser(MoutaiUserAddRequest body) {
        LoginResponse loginResponse = login(body.getMobile(), body.getDeviceId(), body.getVerificationCode());
        MoutaiUser moutaiUser = body.toEntity(loginResponse);
        moutaiUser = saveMoutaiUser(moutaiUser);
        if (Objects.isNull(moutaiUser)) {
            log.error("saveMoutaiUser failed");
            return null;
        }

        if (CollectionUtils.isEmpty(body.getAppointmentInfos())) {
            log.error("appointmentInfos is null or empty");
            return moutaiUser.getId();
        }

        saveAppointments(moutaiUser, body.getAppointmentInfos());

        return moutaiUser.getId();
    }

    /**
     * 查询用户列表
     *
     * @param param 查询参数
     * @return 用户列表
     */
    public Page<MoutaiUser> listMoutaiUser(QueryMoutaiUserParam param) {
        Specification<MoutaiUser> specification = (root, query, criteriaBuilder) -> {
            // 未删除
            Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("isDeleted"), false));
            if (Objects.nonNull(param.getMobile())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("mobile"),
                        "%" + param.getMobile() + "%"));
            }
            return predicate;
        };

        return moutaiUserRepository.findAll(specification, param.toPageable());
    }

    /**
     * 查询用户信息
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    public MoutaiUser getMoutaiUser(Long id) {
        return moutaiUserRepository.findById(id)
                .orElse(null);
    }

    /**
     * 调用 i茅台 登录接口
     *
     * @param mobile   手机号
     * @param deviceId 设备 ID
     * @param code     验证码
     * @return 登录响应数据
     */
    private LoginResponse login(String mobile, String deviceId, String code) {
        Long timestamp = TimeUtil.nowTimestamp();
        LoginRequest requestBody = LoginRequest.builder()
                .mobile(mobile)
                .vCode(code)
                .md5(signature(mobile + code, timestamp))
                .timestamp(timestamp)
                .mtAppVersion(moutaiProperty.getAppVersion())
                .build();

        Response<LoginResponse> response = HttpUtil.request(
                MoutaiUrl.LOGIN_URL,
                new TypeReference<>() {
                },
                MoutaiUtil.getDefaultHeaderMap(deviceId, moutaiProperty.getAppVersion()),
                requestBody);

        if (Objects.isNull(response) || !Integer.valueOf(2000).equals(response.getCode())) {
            log.error("login failed: {}", response);
            return null;
        } else {
            return response.getData();
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMoutaiUser(Long id) {
        if (Objects.isNull(id)) {
            log.error("id is null, not remove");
            return false;
        }
        moutaiUserRepository.findById(id).ifPresent(moutaiUser -> {
            moutaiUser.setIsDeleted(true);
            saveMoutaiUser(moutaiUser);
        });

        moutaiAppointmentRepository.findAll((Specification<MoutaiAppointment>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("moutaiUserId"), id)))
                .forEach(moutaiAppointment -> {
                    moutaiAppointment.setIsDeleted(true);
                    moutaiAppointmentRepository.save(moutaiAppointment);
                });
        return true;
    }

    /**
     * 编辑用户信息
     *
     * @param id   用户 ID
     * @param body 用户信息数据
     * @return 是否编辑成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyMoutaiUser(Long id, MoutaiUserModifyRequest body) {
        if (Objects.isNull(id) || Objects.isNull(body)) {
            log.error("id or body is null, not modify");
            return false;
        }

        MoutaiUser moutaiUser = moutaiUserRepository.findById(id).orElse(null);
        if (Objects.isNull(moutaiUser)) {
            log.error("moutaiUser is null, not modify");
            return false;
        }

        // 编辑手机号信息
        updateMoutaiUserInfo(moutaiUser, body);

        // 删除用户的预约信息
        moutaiAppointmentRepository.findAll((Specification<MoutaiAppointment>) (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("moutaiUserId"), id))
        ).forEach(moutaiAppointment -> {
            moutaiAppointment.setIsDeleted(true);
            moutaiAppointmentRepository.save(moutaiAppointment);
        });

        // 保存预约信息
        saveAppointments(moutaiUser, body.getAppointmentInfos());
        return true;
    }

    /**
     * 预约所有用户
     *
     * @return 是否预约成功
     */
    public boolean reserveAll() {
        List<Long> allUserIdList = moutaiUserRepository.findAll().stream()
                .map(MoutaiUser::getId)
                .toList();
        return reserve(allUserIdList);
    }

    /**
     * 预约
     *
     * @param ids 执行预约的用户 ID 列表
     * @return 是否预约成功
     */
    public boolean reserve(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("ids is null or empty, not reserve");
            return false;
        }

        List<MoutaiUser> moutaiUserList = moutaiUserRepository.findAllById(ids).stream()
                .filter(Objects::nonNull)
                .toList();

        for (MoutaiUser moutaiUser : moutaiUserList) {
            reserve(moutaiUser);
        }

        return true;
    }

    /**
     * 更新用户信息
     *
     * @param moutaiUser 用户信息
     * @param body       请求体
     */
    private void updateMoutaiUserInfo(MoutaiUser moutaiUser, MoutaiUserModifyRequest body) {
        if (Objects.isNull(moutaiUser) || Objects.isNull(body)) {
            log.error("moutaiUser or body is null, not updateMobile");
            return;
        }

        // 手机号不变
        if (moutaiUser.getMobile().equals(body.getMobile())) {
            return;
        }

        // 重新调用 i茅台 登录
        LoginResponse loginResponse = login(body.getMobile(), moutaiUser.getDeviceId(), body.getVerificationCode());
        if (Objects.isNull(loginResponse)) {
            log.error("loginResponse is null, not updateMobile");
            return;
        }

        // 更新用户信息
        moutaiUser.setDeviceId(body.getDeviceId());
        moutaiUser.setUserId(loginResponse.getUserId());
        moutaiUser.setUserName(loginResponse.getUserName());
        moutaiUser.setMobile(body.getMobile());
        //noinspection DuplicatedCode
        moutaiUser.setVerifyStatus(loginResponse.getVerifyStatus());
        moutaiUser.setIdCode(loginResponse.getIdCode());
        moutaiUser.setIdType(loginResponse.getIdType());
        moutaiUser.setToken(loginResponse.getToken());
        moutaiUser.setUserTag(loginResponse.getUserTag());
        moutaiUser.setCookie(loginResponse.getCookie());
        moutaiUser.setDid(loginResponse.getDid());
        moutaiUser.setBirthday(loginResponse.getBirthday());
        saveMoutaiUser(moutaiUser);
    }

    /**
     * 预约
     *
     * @param user 用户信息
     */
    private void reserve(MoutaiUser user) {
        if (Objects.isNull(user)) {
            log.error("user is null, not reserve");
            return;
        }

        List<MoutaiAppointment> moutaiAppointmentList = user.getMoutaiAppointmentList();
        if (Objects.isNull(moutaiAppointmentList) || moutaiAppointmentList.isEmpty()) {
            log.error("moutaiAppointmentList is null or empty");
            return;
        }

        moutaiAppointmentList.forEach(moutaiAppointment -> {
            MoutaiShop moutaiShop = moutaiAppointment.getMoutaiShop();
            if (Objects.isNull(moutaiShop)) {
                log.error("moutaiShop is null");
                return;
            }

            if (Objects.isNull(moutaiAppointment.getMoutaiItemItemCode())) {
                log.error("moutaiItemItemCode is null");
                return;
            }

            // 调用预约接口
            Response<Map<String, Object>> response = getMapResponse(user, moutaiAppointment);
            if (Objects.isNull(response) || !Integer.valueOf(2000).equals(response.getCode())) {
                log.error("reserve failed: {}", response);
                return;
            }

            try {
                Thread.sleep(Duration.ofSeconds(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("reserve error", e);
            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 保存后的用户信息
     */
    private MoutaiUser saveMoutaiUser(MoutaiUser user) {
        if (Objects.isNull(user)) {
            log.error("user is null, not save");
            return null;
        }

        // 保存用户信息
        return moutaiUserRepository.save(user);
    }

    /**
     * 调用预约接口
     *
     * @param user              用户信息
     * @param moutaiAppointment 预约信息
     * @return 预约接口响应
     */
    private Response<Map<String, Object>> getMapResponse(MoutaiUser user, MoutaiAppointment moutaiAppointment) {
        Optional<MoutaiConfig> sessionIdConfigOptional = moutaiConfigRepository.findByKey(Constants.Moutai.SESSION_ID);

        if (sessionIdConfigOptional.isEmpty()) {
            return Response.fail("sessionId 未找到，无法进行预约操作。");
        }

        String sessionId = sessionIdConfigOptional.get()
                .getValue();

        ReserveRequest requestBody = ReserveRequest.builder()
                .userId(user.getUserId())
                .sessionId(Long.valueOf(sessionId))
                .shopId(moutaiAppointment.getMoutaiShop().getShopId())
                .itemInfoList(List.of(ReserveRequest.ItemInfo.builder()
                        .itemId(moutaiAppointment.getMoutaiItemItemCode())
                        .count(1)
                        .build()))
                .build();

        requestBody.setActParam(aesEncode(requestBody));

        Response<Map<String, Object>> response = HttpUtil.request(
                MoutaiUrl.REVERSE_URL,
                new TypeReference<>() {
                },
                MoutaiUtil.getReserveHeaderMap(moutaiAppointment.getMoutaiShop(), user, moutaiProperty.getAppVersion()),
                requestBody);

        log.info("reserve response: {}", response);
        return response;
    }

    /**
     * MD5 签名
     *
     * @param content   内容
     * @param timestamp 时间戳
     * @return 签名后的字符串
     */
    private String signature(String content, Long timestamp) {
        String text = moutaiProperty.getSalt() + content + timestamp;
        String md5;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 signature error", e);
            return null;
        }
        return md5;
    }

    /**
     * AES 加密
     *
     * @param param 请求体
     * @return 加密后的字符串
     */
    private String aesEncode(ReserveRequest param) {
        if (Objects.isNull(param)) {
            return null;
        }

        try {
            @SuppressWarnings("java:S5542")
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(moutaiProperty.getAesKey().getBytes(), "AES"),
                    new IvParameterSpec(moutaiProperty.getAesIv().getBytes()));

            byte[] encryptedBytes = cipher.doFinal(param.getAesEncodeString()
                    .getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("AesEncrypt error", e);
            return null;
        }
    }

    /**
     * 保存预约信息
     *
     * @param moutaiUser             用户信息
     * @param appointmentRequestList 预约信息列表
     */
    private void saveAppointments(MoutaiUser moutaiUser,
                                  List<MoutaiUserAddRequest.AppointmentRequest> appointmentRequestList) {
        if (Objects.isNull(appointmentRequestList) || appointmentRequestList.isEmpty()) {
            log.error("appointmentRequestList is null or empty, not save");
            return;
        }

        // 聚合出来 shopIdList 和 itemIdList
        List<Long> shopIdList = appointmentRequestList.stream()
                .filter(Objects::nonNull)
                .map(MoutaiUserAddRequest.AppointmentRequest::getShopId)
                .toList();
        List<Long> itemIdList = appointmentRequestList.stream()
                .filter(Objects::nonNull)
                .map(MoutaiUserAddRequest.AppointmentRequest::getItemId)
                .toList();

        // 查询店铺信息和商品信息
        Map<Long, MoutaiShop> idToShopMap = moutaiShopRepository.findAllById(shopIdList).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(MoutaiShop::getId, shop -> shop, (a, b) -> a));

        Map<Long, MoutaiItem> idToItemMap = moutaiItemRepository.findAllById(itemIdList).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(MoutaiItem::getId, item -> item, (a, b) -> a));

        List<MoutaiAppointment> moutaiAppointmentList = appointmentRequestList.stream()
                .filter(Objects::nonNull)
                .map(appointmentRequest -> {
                    MoutaiAppointment moutaiAppointment = new MoutaiAppointment();
                    moutaiAppointment.setMoutaiUserId(moutaiUser.getId());
                    moutaiAppointment.setMoutaiShopShopId(
                            idToShopMap.getOrDefault(appointmentRequest.getShopId(), new MoutaiShop()).getShopId()
                    );
                    moutaiAppointment.setMoutaiItemItemCode(
                            idToItemMap.getOrDefault(appointmentRequest.getItemId(), new MoutaiItem()).getItemCode()
                    );
                    return moutaiAppointment;
                })
                .toList();
        saveMoutaiAppointments(moutaiAppointmentList);
    }

    /**
     * 保存预约信息
     *
     * @param moutaiAppointmentList 预约信息列表
     */
    private void saveMoutaiAppointments(List<MoutaiAppointment> moutaiAppointmentList) {
        if (Objects.isNull(moutaiAppointmentList) || moutaiAppointmentList.isEmpty()) {
            log.error("moutaiAppointmentList is null or empty, not save");
            return;
        }

        // 保存预约信息
        moutaiAppointmentRepository.saveAll(moutaiAppointmentList);
    }
}
