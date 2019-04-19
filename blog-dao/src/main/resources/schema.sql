DROP DATABASE IF EXISTS blogdb;

CREATE DATABASE blogdb
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE blogdb;

-- ----------------------------
--  Table structure for tb_user
-- ----------------------------
DROP TABLE
  IF EXISTS tb_user;

CREATE TABLE tb_user
(
  user_id      BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
    COMMENT '用户ID',
  email        VARCHAR(64)                           NOT NULL
    COMMENT '电子邮件',
  password     VARCHAR(64)                           NOT NULL
    COMMENT '密码',
  salt         VARCHAR(64)                           NOT NULL
    COMMENT '密码盐',
  is_deleted   TINYINT                               NOT NULL DEFAULT 0
    COMMENT '逻辑删除',
  created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
    COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间'
)
  COMMENT '用户表';
CREATE UNIQUE INDEX tb_email_UNIQUE
  ON tb_user (email);

-- ----------------------------
--  Table structure for tb_user_profile
-- ----------------------------
DROP TABLE
  IF EXISTS tb_user_profile;

CREATE TABLE tb_user_profile
(
  user_id      BIGINT(20)   NOT NULL
    COMMENT '用户ID',
  name         VARCHAR(32)  NOT NULL DEFAULT ''
    COMMENT '姓名',
  avatar       VARCHAR(128) NOT NULL DEFAULT ''
    COMMENT '头像',
  id_type      VARCHAR(1)   NOT NULL DEFAULT '0'
    COMMENT '证件类型',
  id_no        VARCHAR(128) NOT NULL DEFAULT ''
    COMMENT '证件号码',
  ip_address   VARCHAR(20)  NOT NULL DEFAULT ''
    COMMENT 'IP地址',
  is_deleted   TINYINT      NOT NULL DEFAULT 0
    COMMENT '逻辑删除',
  created_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
    COMMENT '创建时间',
  updated_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间'
)
  COMMENT '用户信息表';
CREATE UNIQUE INDEX user_id_UNIQUE
  ON tb_user_profile (user_id);

-- ----------------------------
--  Table structure for tb_role
-- ----------------------------
DROP TABLE
  IF EXISTS tb_role;

CREATE TABLE tb_role
(
  role_id      BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
    COMMENT '主键, 自增',
  role_code    VARCHAR(32)                           NOT NULL
    COMMENT '角色代码',
  role_name    VARCHAR(32)                           NOT NULL
    COMMENT '角色名称',
  is_deleted   TINYINT                               NOT NULL DEFAULT 0
    COMMENT '逻辑删除',
  created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
    COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间'
)
  COMMENT '角色表';
CREATE UNIQUE INDEX role_code_UNIQUE
  ON tb_role (role_code);

-- ----------------------------
--  Table structure for tb_menu
-- ----------------------------
DROP TABLE
  IF EXISTS tb_menu;

CREATE TABLE tb_menu
(
  menu_id      BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
    COMMENT '主键, 自增',
  menu_code    VARCHAR(32)                           NOT NULL
    COMMENT '菜单代码',
  menu_name    VARCHAR(32)                           NOT NULL
    COMMENT '菜单名称',
  parent_code  VARCHAR(32)                           NOT NULL DEFAULT ''
    COMMENT '父菜单代码',
  sort         INT(11)                               NOT NULL DEFAULT 0
    COMMENT '菜单排序(从0开始)',
  icon         VARCHAR(128)                          NOT NULL DEFAULT ''
    COMMENT '菜单图标的样式',
  is_deleted   TINYINT                               NOT NULL DEFAULT 0
    COMMENT '逻辑删除',
  created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
    COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间'
)
  COMMENT '菜单表';
CREATE INDEX sort_ix
  ON tb_menu (sort);
CREATE UNIQUE INDEX menu_code_UNIQUE
  ON tb_menu (menu_code);

-- ----------------------------
--  Table structure for tb_user_role
-- ----------------------------
DROP TABLE
  IF EXISTS tb_user_role;

CREATE TABLE tb_user_role
(
  user_id BIGINT(20) NOT NULL
    COMMENT '用户ID',
  role_id BIGINT(20) NOT NULL
    COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id)
)
  COMMENT '用户角色表';

-- ----------------------------
--  Table structure for rtb_ole_menu
-- ----------------------------
DROP TABLE
  IF EXISTS tb_role_menu;

CREATE TABLE tb_role_menu
(
  role_id BIGINT(20) NOT NULL
    COMMENT '角色ID',
  menu_id BIGINT(20) NOT NULL
    COMMENT '菜单ID',
  PRIMARY KEY (role_id, menu_id)
)
  COMMENT '角色菜单表';

-- ----------------------------
--  Table structure for tb_dict
-- ----------------------------
DROP TABLE
  IF EXISTS tb_dict;

CREATE TABLE tb_dict
(
  dict_id      BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
    COMMENT '字典ID',
  dict_type    VARCHAR(20)                           NOT NULL
    COMMENT '字典类型',
  dict_code    VARCHAR(64)                           NOT NULL
    COMMENT '字典代码',
  value        VARCHAR(256)                          NOT NULL
    COMMENT '值',
  remark       VARCHAR(256)                          NOT NULL DEFAULT ''
    COMMENT '备注',
  sort         INT(11)                               NOT NULL DEFAULT 0
    COMMENT '排序（从0开始）',
  is_deleted   TINYINT                               NOT NULL DEFAULT 0
    COMMENT '逻辑删除',
  created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
    COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间'
)
  COMMENT '字典表';
CREATE UNIQUE INDEX type_code_UNIQUE
  ON tb_dict (dict_type, dict_code);

#====================初始数据====================#

-- ----------------------------
--  data for tb_user
-- ----------------------------
INSERT INTO tb_user
(user_id, email, PASSWORD, salt)
VALUES
  # 密码：11111111
  (1, 'admin@kangyonggan.com', '8d0d54520fe0466ac80827d9f2f038b22e3c7c2d', 'd820c214488d7c6f');

INSERT INTO tb_user_profile
(user_id, name, id_type, id_no, ip_address)
VALUES (1, '管理员', '0', '', '127.0.0.1');

-- ----------------------------
--  data for tb_role
-- ----------------------------
INSERT INTO tb_role
(role_id, role_code, role_name)
VALUES (1, 'ROLE_ADMIN', '管理员');

-- ----------------------------
--  data for tb_menu
-- ----------------------------
INSERT INTO tb_menu
(menu_code, menu_name, parent_code, sort, icon)
VALUES ('SYSTEM', '系统', '', 0, 'ios-cog'),
       ('SYSTEM_USER', '用户管理', 'SYSTEM', 0, ''),
       ('SYSTEM_ROLE', '角色管理', 'SYSTEM', 1, ''),
       ('SYSTEM_MENU', '菜单管理', 'SYSTEM', 2, ''),
       ('SYSTEM_DICT', '数据字典', 'SYSTEM', 3, ''),

       ('USER', '我的', '', 1, 'ios-person'),
       ('USER_PROFILE', '个人资料', 'USER', 0, '');

-- ----------------------------
--  data for tb_user_role
-- ----------------------------
INSERT INTO tb_user_role
VALUES (1, 1);

-- ----------------------------
--  data for tb_role_menu
-- ----------------------------
INSERT INTO tb_role_menu
SELECT 1,
       menu_id
FROM tb_menu;

-- ----------------------------
--  data for tb_dict
-- ----------------------------
INSERT INTO tb_dict
(dict_type, dict_code, value, sort)
VALUES ('ID_TYPE', '0', '身份证', 0);