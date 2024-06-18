DROP SCHEMA IF EXISTS fuck_moutai;
CREATE SCHEMA IF NOT EXISTS fuck_moutai;
USE fuck_moutai;

# ------------------------- iMoutai 相关表 -------------------------
CREATE TABLE IF NOT EXISTS moutai_item
(
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',

    item_code  VARCHAR(64)      NOT NULL COMMENT '编码',
    title      VARCHAR(128)     NULL COMMENT '标题',
    content    VARCHAR(512)     NULL COMMENT '内容',
    picture    VARCHAR(1024)    NULL COMMENT '图片',
    picture_v2 VARCHAR(1024)    NULL COMMENT '图片',
    jump_url   VARCHAR(1024)    NULL COMMENT '跳转链接',
    check_tag  INTEGER UNSIGNED NULL COMMENT '未知属性',
    count      INTEGER UNSIGNED NULL COMMENT '数量',

    created_at DATETIME         NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at DATETIME         NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    created_by BIGINT UNSIGNED  NOT NULL COMMENT '创建人',
    updated_by BIGINT UNSIGNED  NOT NULL COMMENT '更新人',
    is_deleted BOOLEAN          NOT NULL DEFAULT FALSE COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台商品表';

CREATE UNIQUE INDEX u_inx_item_code
    ON moutai_item (item_code);

CREATE TABLE IF NOT EXISTS moutai_shop
(
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',

    shop_id         VARCHAR(32)      NOT NULL COMMENT '店 ID',
    name            VARCHAR(128)     NULL COMMENT '店名',
    address         VARCHAR(256)     NULL COMMENT '地址',
    full_address    VARCHAR(256)     NULL COMMENT '完整地址',
    tenant_name     VARCHAR(256)     NULL COMMENT '公司名',
    tags            VARCHAR(256)     NULL COMMENT '标签',
    open_end_time   TIME             NULL COMMENT '关门时间',
    open_start_time TIME             NULL COMMENT '开门时间',
    province        INTEGER UNSIGNED NULL COMMENT '省份 Code',
    province_name   VARCHAR(32)      NULL COMMENT '省份名',
    city            INTEGER UNSIGNED NULL COMMENT '城市 Code',
    city_name       VARCHAR(64)      NULL COMMENT '城市名',
    district        INTEGER UNSIGNED NULL COMMENT '区县 Code',
    district_name   VARCHAR(64)      NULL COMMENT '区县名',
    lng             DECIMAL(16, 6)   NULL COMMENT '经度',
    lat             DECIMAL(16, 6)   NULL COMMENT '维度',
    layaway         BOOLEAN          NULL COMMENT '是否支持分期付款',

    created_at      DATETIME         NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at      DATETIME         NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    created_by      BIGINT UNSIGNED  NOT NULL COMMENT '创建人',
    updated_by      BIGINT UNSIGNED  NOT NULL COMMENT '更新人',
    is_deleted      BOOLEAN          NOT NULL DEFAULT FALSE COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台门店信息表';

CREATE UNIQUE INDEX u_inx_shop_id
    ON moutai_shop (shop_id);

CREATE TABLE IF NOT EXISTS moutai_user
(
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',

    device_id     CHAR(36)        NOT NULL COMMENT '设备 ID',

    user_id       BIGINT UNSIGNED NOT NULL COMMENT '用户 ID',
    user_name     VARCHAR(64)     NOT NULL COMMENT '用户名',
    mobile        VARCHAR(16)     NOT NULL COMMENT '手机号',
    verify_status INTEGER         NOT NULL COMMENT '认证状态',
    id_code       VARCHAR(32)     NOT NULL COMMENT '身份证号',
    id_type       INTEGER         NOT NULL COMMENT '证件类型',
    token         VARCHAR(1024)   NOT NULL COMMENT 'Token',
    user_tag      INTEGER         NULL COMMENT '用户标签',
    cookie        VARCHAR(1024)   NOT NULL COMMENT 'Cookie',
    did           VARCHAR(1024)   NOT NULL COMMENT '设备 ID',
    birthday      VARCHAR(32)     NULL COMMENT '生日',

    created_at    DATETIME        NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at    DATETIME        NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    created_by    BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    updated_by    BIGINT UNSIGNED NOT NULL COMMENT '更新人',
    is_deleted    BOOLEAN         NOT NULL DEFAULT FALSE COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台用户信息表';

CREATE UNIQUE INDEX u_inx_mobile
    ON moutai_user (mobile);

CREATE TABLE IF NOT EXISTS moutai_appointment
(
    id                    BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',

    moutai_user_id        BIGINT UNSIGNED NOT NULL COMMENT '用户 ID',
    moutai_shop_shop_id   VARCHAR(32)     NOT NULL COMMENT '店 ID',
    moutai_item_item_code VARCHAR(64)     NOT NULL COMMENT '编码',

    created_at            DATETIME        NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at            DATETIME        NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    created_by            BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    updated_by            BIGINT UNSIGNED NOT NULL COMMENT '更新人',
    is_deleted            BOOLEAN         NOT NULL DEFAULT FALSE COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台预约信息表';

CREATE TABLE IF NOT EXISTS moutai_config
(
    id    BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
    `key` VARCHAR(64)   NOT NULL COMMENT '键',
    value VARCHAR(1024) NOT NULL COMMENT '值'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台配置表';

CREATE TABLE IF NOT EXISTS moutai_appointment_record
(
    id             BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',

    moutai_user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户 ID',
    reserve_time   DATETIME         NOT NULL COMMENT '预约时间',
    reserve_status INTEGER UNSIGNED NOT NULL COMMENT '预约状态',

    created_at     DATETIME         NOT NULL DEFAULT NOW() COMMENT '创建时间',
    updated_at     DATETIME         NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    created_by     BIGINT UNSIGNED  NOT NULL COMMENT '创建人',
    updated_by     BIGINT UNSIGNED  NOT NULL COMMENT '更新人',
    is_deleted     BOOLEAN          NOT NULL DEFAULT FALSE COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '茅台预约记录表';


# ------------------------- 系统相关表 -------------------------
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user
(
    id                  BIGINT UNSIGNED          NOT NULL PRIMARY KEY COMMENT '主键 ID',

    username            VARCHAR(64)              NOT NULL COMMENT '用户名',
    password            VARCHAR(128)             NOT NULL COMMENT '密码（使用 BCrypt 加密）',
    email               VARCHAR(128)             NOT NULL COMMENT '邮箱',
    phone               VARCHAR(32)              NOT NULL COMMENT '手机号',
    avatar              VARCHAR(255)             NULL COMMENT '头像',
    nickname            VARCHAR(64)              NULL COMMENT '昵称',
    expired             BOOLEAN    DEFAULT FALSE NOT NULL COMMENT '是否过期',
    locked              BOOLEAN    DEFAULT FALSE NOT NULL COMMENT '是否锁定',
    credentials_expired BOOLEAN    DEFAULT FALSE NOT NULL COMMENT '是否凭证过期',
    enabled             BOOLEAN    DEFAULT TRUE  NOT NULL COMMENT '是否启用',

    created_at          DATETIME   DEFAULT NOW() NOT NULL COMMENT '创建时间',
    updated_at          DATETIME   DEFAULT NOW() NOT NULL ON UPDATE NOW() COMMENT '更新时间',
    created_by          BIGINT UNSIGNED          NOT NULL COMMENT '创建人',
    updated_by          BIGINT UNSIGNED          NOT NULL COMMENT '更新人',
    is_deleted          TINYINT(1) DEFAULT 0     NOT NULL COMMENT '是否删除'
) ENGINE = InnoDB
  CHARSET = UTF8MB4
    COMMENT = '用户表';