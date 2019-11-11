/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MariaDB
 Source Server Version : 100309
 Source Host           : 127.0.0.1:3306
 Source Schema         : file

 Target Server Type    : MariaDB
 Target Server Version : 100309
 File Encoding         : 65001

 Date: 11/11/2019 14:57:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bs_attach
-- ----------------------------
DROP TABLE IF EXISTS `bs_attach`;
CREATE TABLE `bs_attach`  (
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `SYSTEM_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '系统id',
  `FUNCTION_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '功能id',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `UPDATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `DEL` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bs_attach_detail
-- ----------------------------
DROP TABLE IF EXISTS `bs_attach_detail`;
CREATE TABLE `bs_attach_detail`  (
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `ATTACH_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '附件组id',
  `URL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '相对位置带文件名',
  `SIZE` bigint(20) NULL DEFAULT 0 COMMENT '大小',
  `TRUNK` bigint(20) NULL DEFAULT 1 COMMENT '分片总数（若大于1则代表',
  `SOURCE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原文件名',
  `SUFFIX` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '后缀',
  `PARENT_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '分片关联的附件id、',
  `DEL` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `UPDATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bs_attach_detail_record
-- ----------------------------
DROP TABLE IF EXISTS `bs_attach_detail_record`;
CREATE TABLE `bs_attach_detail_record`  (
  `SEQ_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件详情id',
  `ATTACH_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '附件组id',
  `URL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '相对位置带文件名',
  `SIZE` bigint(20) NULL DEFAULT 0 COMMENT '大小',
  `TRUNK` bigint(20) NULL DEFAULT 1 COMMENT '分片数',
  `SOURCE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原文件名',
  `PARENT_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '关联的附件详情id',
  `SUFFIX` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '后缀',
  `DEL` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `UPDATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `OPERATOR_STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '历史操作状态',
  PRIMARY KEY (`SEQ_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bs_attach_record
-- ----------------------------
DROP TABLE IF EXISTS `bs_attach_record`;
CREATE TABLE `bs_attach_record`  (
  `SEQ_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作得附件组id',
  `SYSTEM_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '系统id',
  `FUNCTION_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '功能id',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `UPDATE_TIME` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `DEL` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除',
  `OPERATOR_STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '历史操作状态',
  PRIMARY KEY (`SEQ_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件组历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `NAME` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司名',
  `USER_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `PARENT_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '上级公司id',
  `STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_company
-- ----------------------------
INSERT INTO `sys_company` VALUES ('1', '测试公司', '1', '2019-11-08 15:39:02', '', '1');

-- ----------------------------
-- Table structure for sys_company_system
-- ----------------------------
DROP TABLE IF EXISTS `sys_company_system`;
CREATE TABLE `sys_company_system`  (
  `COMPANY_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司id',
  `SYSTEM_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统id',
  PRIMARY KEY (`COMPANY_ID`, `SYSTEM_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司系统表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_company_system
-- ----------------------------
INSERT INTO `sys_company_system` VALUES ('1', '1');

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `NAME` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '功能名称',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp(0) NULL DEFAULT current_timestamp(),
  `PARENT_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '上级功能id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_system
-- ----------------------------
DROP TABLE IF EXISTS `sys_system`;
CREATE TABLE `sys_system`  (
  `ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `NAME` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统名',
  `TOKEN` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '令牌',
  `USER_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `PARENT_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级系统id',
  `STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态',
  `Q_AUTH` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '查询权限',
  `S_AUTH` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '新增权限',
  `U_AUTH` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '修改权限',
  `D_AUTH` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '删除权限',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_system
-- ----------------------------
INSERT INTO `sys_system` VALUES ('1', '测试上传', '1', '1', '2019-11-08 15:38:44', NULL, '1', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for sys_system_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_system_function`;
CREATE TABLE `sys_system_function`  (
  `SYSTEM_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统id',
  `FUNCTION_ID` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能id',
  PRIMARY KEY (`SYSTEM_ID`, `FUNCTION_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
