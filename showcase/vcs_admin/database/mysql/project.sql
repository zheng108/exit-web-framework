/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : project

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2012-05-14 22:34:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_data_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `tb_data_dictionary`;
CREATE TABLE `tb_data_dictionary` (
  `id` char(32) NOT NULL,
  `name` varchar(512) NOT NULL,
  `pin_yin_code` varchar(512) DEFAULT NULL,
  `remark` text,
  `type` varchar(1) NOT NULL,
  `value` varchar(64) NOT NULL,
  `wubi_code` varchar(512) DEFAULT NULL,
  `fk_category_id` char(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB034107A704DF8AD` (`fk_category_id`),
  CONSTRAINT `FKB034107A704DF8AD` FOREIGN KEY (`fk_category_id`) REFERENCES `tb_dictionary_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_data_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_dictionary_category`
-- ----------------------------
DROP TABLE IF EXISTS `tb_dictionary_category`;
CREATE TABLE `tb_dictionary_category` (
  `id` char(32) NOT NULL,
  `code` varchar(128) NOT NULL,
  `name` varchar(256) NOT NULL,
  `remark` text,
  `fk_parent_id` char(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `FK181DDF36F99D2701` (`fk_parent_id`),
  CONSTRAINT `FK181DDF36F99D2701` FOREIGN KEY (`fk_parent_id`) REFERENCES `tb_dictionary_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_dictionary_category
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_group`
-- ----------------------------
DROP TABLE IF EXISTS `tb_group`;
CREATE TABLE `tb_group` (
  `id` char(32) NOT NULL,
  `name` varchar(64) NOT NULL,
  `remark` text,
  `state` int(11) NOT NULL,
  `type` varchar(2) NOT NULL,
  `fk_parent_id` char(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKFA285D6EE76F3D70` (`fk_parent_id`),
  CONSTRAINT `FKFA285D6EE76F3D70` FOREIGN KEY (`fk_parent_id`) REFERENCES `tb_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group
-- ----------------------------
INSERT INTO `tb_group` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0002', '超级管理员', null, '1', '03', null);

-- ----------------------------
-- Table structure for `tb_group_resource`
-- ----------------------------
DROP TABLE IF EXISTS `tb_group_resource`;
CREATE TABLE `tb_group_resource` (
  `fk_resource_id` char(32) NOT NULL,
  `fk_group_id` char(32) NOT NULL,
  KEY `FK898FD3BF9CE20AF` (`fk_group_id`),
  KEY `FK898FD3BFE0485F85` (`fk_resource_id`),
  CONSTRAINT `FK898FD3BF9CE20AF` FOREIGN KEY (`fk_group_id`) REFERENCES `tb_group` (`id`),
  CONSTRAINT `FK898FD3BFE0485F85` FOREIGN KEY (`fk_resource_id`) REFERENCES `tb_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group_resource
-- ----------------------------
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0003', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0004', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0005', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0006', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0007', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0008', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0009', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0010', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0011', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0012', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0013', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0014', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0015', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0016', 'SJDK3849CKMS3849DJCK2039ZMSK0002');

-- ----------------------------
-- Table structure for `tb_group_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_group_user`;
CREATE TABLE `tb_group_user` (
  `fk_group_id` char(32) NOT NULL,
  `fk_user_id` char(32) NOT NULL,
  KEY `FK92B07BFC9CE20AF` (`fk_group_id`),
  KEY `FK92B07BFCE614E7E5` (`fk_user_id`),
  CONSTRAINT `FK92B07BFC9CE20AF` FOREIGN KEY (`fk_group_id`) REFERENCES `tb_group` (`id`),
  CONSTRAINT `FK92B07BFCE614E7E5` FOREIGN KEY (`fk_user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group_user
-- ----------------------------
INSERT INTO `tb_group_user` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0002', 'SJDK3849CKMS3849DJCK2039ZMSK0001');

-- ----------------------------
-- Table structure for `tb_resource`
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `id` char(32) NOT NULL,
  `permission` varchar(64) DEFAULT NULL,
  `remark` text,
  `sort` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `type` varchar(2) NOT NULL,
  `value` varchar(512) DEFAULT NULL,
  `fk_parent_id` char(32) DEFAULT NULL,
  `icon` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `text` (`name`),
  UNIQUE KEY `permission` (`permission`),
  KEY `FKECCF42BF7BAAAEE9` (`fk_parent_id`),
  CONSTRAINT `FKECCF42BF7BAAAEE9` FOREIGN KEY (`fk_parent_id`) REFERENCES `tb_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0003', null, null, '1', '权限管理', '01', '#', null, 'security32_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0004', 'user:view', null, '2', '用户管理', '01', '/account/user/view/**', 'SJDK3849CKMS3849DJCK2039ZMSK0003', 'user24_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0005', 'user:create', null, '3', '创建用户', '02', '/account/user/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0004', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0006', 'user:update', null, '4', '修改用户', '02', '/account/user/update/**', 'SJDK3849CKMS3849DJCK2039ZMSK0004', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0007', 'user:delete', null, '5', '删除用户', '02', '/account/user/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0004', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0008', 'user:edit', null, '6', '编辑用户', '02', '/account/user/read/**', 'SJDK3849CKMS3849DJCK2039ZMSK0004', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0009', 'group:view', null, '7', '组管理', '01', '/account/group/view/**', 'SJDK3849CKMS3849DJCK2039ZMSK0003', 'group24_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0010', 'resource:view', null, '8', '资源管理', '01', '/account/resource/view/**', 'SJDK3849CKMS3849DJCK2039ZMSK0003', 'resource24_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0011', 'group:create', null, '9', '创建组', '02', '/account/group/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0012', 'group:update', null, '10', '修改组', '02', '/account/group/update/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0013', 'group:delete', null, '11', '删除组', '02', '/account/group/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0014', 'resource:create', null, '12', '创建资源', '02', '/account/resource/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0015', 'resource:update', null, '13', '修改资源', '02', '/account/resource/update/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0016', 'resource:delete', null, '14', '删除资源', '02', '/account/resource/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` char(32) NOT NULL,
  `email` varchar(256) DEFAULT NULL,
  `password` char(32) NOT NULL,
  `realname` varchar(128) NOT NULL,
  `state` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0001', '27637461@qq.com', 'admin', '系统管理员', '1', 'admin');
