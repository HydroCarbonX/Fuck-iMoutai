package io.hydrocarbon.moutai.controller.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiUser;
import io.hydrocarbon.moutai.param.Response;
import io.hydrocarbon.moutai.param.request.moutai.QueryMoutaiUserParam;
import io.hydrocarbon.moutai.param.request.moutai.MoutaiUserAddRequest;
import io.hydrocarbon.moutai.param.request.moutai.MoutaiUserModifyRequest;
import io.hydrocarbon.moutai.param.response.moutai.MoutaiUserResponse;
import io.hydrocarbon.moutai.service.MoutaiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Zou Zhenfeng
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/moutai/user")
@RequiredArgsConstructor
@Slf4j
public class MoutaiUserController {

    private final MoutaiService moutaiService;

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return 设备 ID
     */
    @PostMapping("/verification-code")
    public Response<String> sendVerificationCode(@RequestParam
                                                 String mobile) {
        String deviceId = moutaiService.sendVerificationCode(mobile);
        return Response.success(deviceId);
    }

    /**
     * 添加用户
     *
     * @param body 用户信息
     * @return 添加成功/失败 对应的消息
     */
    @PostMapping
    public Response<Long> addMoutaiUser(@RequestBody
                                        @Valid
                                        MoutaiUserAddRequest body) {
        Long id = moutaiService.saveMoutaiUser(body);
        if (Objects.isNull(id)) {
            return Response.fail("添加失败，请检查参数！");
        }
        return Response.success(id);
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     * @return 删除成功/失败 对应的消息
     */
    @DeleteMapping("/{id}")
    public Response<String> deleteMoutaiUser(@PathVariable
                                             Long id) {
        boolean result = moutaiService.removeMoutaiUser(id);
        if (result) {
            return Response.success("删除成功");
        }
        return Response.fail("删除失败，请检查参数！");
    }

    /**
     * 查询用户
     *
     * @param mobile   手机号
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    @GetMapping
    public Response<Page<MoutaiUserResponse>> listMoutaiUser(@RequestParam
                                                             String mobile,
                                                             @RequestParam(defaultValue = "0")
                                                             Integer pageNo,
                                                             @RequestParam(defaultValue = "10")
                                                             Integer pageSize) {
        QueryMoutaiUserParam param = QueryMoutaiUserParam.builder()
                .mobile(mobile)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
        Page<MoutaiUser> moutaiUserPage = moutaiService.listMoutaiUser(param);
        return Response.success(moutaiUserPage.map(MoutaiUserResponse::fromEntity));
    }

    /**
     * 查询用户
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Response<MoutaiUserResponse> getMoutaiUser(@PathVariable
                                                      Long id) {
        MoutaiUser moutaiUser = moutaiService.getMoutaiUser(id);
        return Response.success(MoutaiUserResponse.fromEntity(moutaiUser));
    }

    /**
     * 更新用户
     *
     * @param id   用户 ID
     * @param body 用户信息
     * @return 更新成功/失败 对应的消息
     */
    @PutMapping("/{id}")
    public Response<String> updateMoutaiUser(@PathVariable
                                             Long id,
                                             @RequestBody
                                             MoutaiUserModifyRequest body) {
        boolean result = moutaiService.modifyMoutaiUser(id, body);
        if (result) {
            return Response.success("更新成功");
        }
        return Response.fail("更新失败，请检查参数！");
    }

    /**
     * 对指定的用户进行预约操作
     *
     * @param ids 用户 ID
     * @return 预约成功/失败 对应的消息
     */
    @GetMapping("/reserve")
    public Response<String> reverseMoutaiUser(@RequestBody
                                              List<Long> ids) {
        boolean result = moutaiService.reserve(ids);
        if (result) {
            return Response.success("预约成功");
        }
        return Response.fail("预约失败，请检查参数！");
    }
}
