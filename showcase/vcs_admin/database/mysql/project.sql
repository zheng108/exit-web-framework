/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : project

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2012-06-10 20:14:37
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
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d481b6920001', '启用', 'QY', null, 'I', '1', 'YE', '402881e437d467d80137d46fc0e50001');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d481dda30002', '禁用', 'JY', null, 'I', '2', 'SE', '402881e437d467d80137d46fc0e50001');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d481f23a0003', '删除', 'SC', null, 'I', '3', 'MB', '402881e437d467d80137d46fc0e50001');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d4870b230005', 'String', 'STRING', null, 'S', 'S', 'STRING', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d487328e0006', 'Integer', 'INTEGER', null, 'S', 'I', 'INTEGER', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d487a3af0007', 'Long', 'LONG', null, 'S', 'L', 'LONG', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d487e23a0008', 'Double', 'DOUBLE', null, 'S', 'N', 'DOUBLE', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d488416d0009', 'Date', 'DATE', null, 'S', 'D', 'DATE', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d47b250137d4885686000a', 'Boolean', 'BOOLEAN', null, 'S', 'B', 'BOOLEAN', '402881e437d47b250137d485274b0004');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d49e430137d4a5e8570003', '菜单类型', 'CDLX', null, 'S', '01', 'AUOG', '402881e437d467d80137d4709b9c0002');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d49e430137d4a61cec0004', '资源类型', 'AQLX', null, 'S', '02', 'PWOG', '402881e437d467d80137d4709b9c0002');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d49e430137d4a6f1aa0005', '部门', 'BM', null, 'S', '02', 'UU', '402881e437d467d80137d4712ca70003');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d49e430137d4a7783d0006', '机构', 'JG', null, 'S', '01', 'SS', '402881e437d467d80137d4712ca70003');
INSERT INTO `tb_data_dictionary` VALUES ('402881e437d49e430137d4a7ba1a0007', '角色组', 'JSZ', '1', 'S', '03', 'QQX', '402881e437d467d80137d4712ca70003');

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
INSERT INTO `tb_dictionary_category` VALUES ('402881e437d467d80137d46fc0e50001', 'state', '状态', null, null);
INSERT INTO `tb_dictionary_category` VALUES ('402881e437d467d80137d4709b9c0002', 'resource-type', '资源类型', null, null);
INSERT INTO `tb_dictionary_category` VALUES ('402881e437d467d80137d4712ca70003', 'group-type', '组类型', null, null);
INSERT INTO `tb_dictionary_category` VALUES ('402881e437d47b250137d485274b0004', 'value-type', '值类型', null, null);

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
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0017', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0018', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0019', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0020', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0021', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0022', 'SJDK3849CKMS3849DJCK2039ZMSK0002');
INSERT INTO `tb_group_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0023', 'SJDK3849CKMS3849DJCK2039ZMSK0002');

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
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0011', 'group:save', null, '9', '创建和编辑组', '02', '/account/group/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0012', 'group:update', null, '10', '修改组', '02', '/account/group/update/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0013', 'group:delete', null, '11', '删除组', '02', '/account/group/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0009', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0014', 'resource:save', null, '12', '创建和编辑资源', '02', '/account/resource/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0015', 'resource:update', null, '13', '修改资源', '02', '/account/resource/update/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0016', 'resource:delete', null, '14', '删除资源', '02', '/account/resource/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0010', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0017', null, null, '15', '系统管理', '01', '#', null, 'system32_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0018', 'data-dictionary:view', null, '16', '数据字典管理', '01', '/foundation/data-dictionary/view/**', 'SJDK3849CKMS3849DJCK2039ZMSK0017', 'data_dictionary32_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0019', 'dictionary-category:view', null, '17', '字典类别管理', '01', '/foundation/dictionary-category/view/**', 'SJDK3849CKMS3849DJCK2039ZMSK0017', 'dictionary_category32_icon');
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0020', 'dictionary-category:save', null, '18', '创建和编辑字典类别', '02', '/foundation/dictionary-category/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0019', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0021', 'dictionary-category:delete', null, '19', '删除字典类别', '02', '/foundation/dictionary-category/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0019', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0022', 'data-dictionary:save', null, '20', '创建和编辑数据字典', '02', '/foundation/data-dictionary/save/**', 'SJDK3849CKMS3849DJCK2039ZMSK0018', null);
INSERT INTO `tb_resource` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0023', 'data-dictionary:delete', null, '21', '删除数据字典', '02', '/foundation/data-dictionary/delete/**', 'SJDK3849CKMS3849DJCK2039ZMSK0018', null);

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
INSERT INTO `tb_user` VALUES ('SJDK3849CKMS3849DJCK2039ZMSK0001', '27637461@qq.com', 'admin', 'vincent.chen', '1', 'admin');
