/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : test04

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 21/03/2022 19:29:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept` (
  `dept_id` bigint(20) NOT NULL COMMENT '主键ID（全局唯一）',
  `dept_name` varchar(30) DEFAULT NULL COMMENT '部门名称',
  `staff` int(11) DEFAULT NULL COMMENT '员工',
  `tel` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `deleted` bit(1) DEFAULT b'0' COMMENT '逻辑删除（0:未删除；1:已删除）',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Records of tb_dept
-- ----------------------------
BEGIN;
INSERT INTO `tb_dept` (`dept_id`, `dept_name`, `staff`, `tel`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (10, 'Java', 20, '88886666', b'0', 4, '2020-10-30 11:48:19', '2021-05-24 15:11:17');
INSERT INTO `tb_dept` (`dept_id`, `dept_name`, `staff`, `tel`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (11, 'Mysql', 12, '80802121', b'0', 0, '2020-10-30 11:48:44', '2021-05-24 15:11:20');
INSERT INTO `tb_dept` (`dept_id`, `dept_name`, `staff`, `tel`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (12, 'Tomcat', 12, '23231212', b'0', 0, '2020-10-30 11:48:44', '2021-05-24 15:11:23');
INSERT INTO `tb_dept` (`dept_id`, `dept_name`, `staff`, `tel`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (13, 'Nginx', 13, '7116201', b'0', 0, '2020-10-30 11:48:45', '2021-05-24 15:11:26');
COMMIT;

-- ----------------------------
-- Table structure for tb_stu_sub_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_stu_sub_relation`;
CREATE TABLE `tb_stu_sub_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `stu_id` int(11) DEFAULT NULL COMMENT '学号ID',
  `sub_id` int(11) DEFAULT NULL COMMENT '学生ID',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`),
  KEY `stu_id` (`stu_id`),
  KEY `sub_id` (`sub_id`),
  CONSTRAINT `tb_stu_sub_relation_ibfk_1` FOREIGN KEY (`stu_id`) REFERENCES `tb_student` (`stu_id`),
  CONSTRAINT `tb_stu_sub_relation_ibfk_2` FOREIGN KEY (`sub_id`) REFERENCES `tb_subject` (`sub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='学生与课程关系表';

-- ----------------------------
-- Records of tb_stu_sub_relation
-- ----------------------------
BEGIN;
INSERT INTO `tb_stu_sub_relation` (`id`, `stu_id`, `sub_id`, `score`) VALUES (1, 1, 1, 88);
INSERT INTO `tb_stu_sub_relation` (`id`, `stu_id`, `sub_id`, `score`) VALUES (2, 1, 2, 67);
INSERT INTO `tb_stu_sub_relation` (`id`, `stu_id`, `sub_id`, `score`) VALUES (3, 2, 1, 82);
COMMIT;

-- ----------------------------
-- Table structure for tb_student
-- ----------------------------
DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student` (
  `stu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学号ID',
  `stu_name` varchar(30) DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`stu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='学生表';

-- ----------------------------
-- Records of tb_student
-- ----------------------------
BEGIN;
INSERT INTO `tb_student` (`stu_id`, `stu_name`) VALUES (1, '张三');
INSERT INTO `tb_student` (`stu_id`, `stu_name`) VALUES (2, '李四');
INSERT INTO `tb_student` (`stu_id`, `stu_name`) VALUES (3, '王五');
COMMIT;

-- ----------------------------
-- Table structure for tb_subject
-- ----------------------------
DROP TABLE IF EXISTS `tb_subject`;
CREATE TABLE `tb_subject` (
  `sub_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `sub_name` varchar(30) DEFAULT NULL COMMENT '课程名',
  PRIMARY KEY (`sub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='课程表';

-- ----------------------------
-- Records of tb_subject
-- ----------------------------
BEGIN;
INSERT INTO `tb_subject` (`sub_id`, `sub_name`) VALUES (1, '英语');
INSERT INTO `tb_subject` (`sub_id`, `sub_name`) VALUES (2, '数学');
INSERT INTO `tb_subject` (`sub_id`, `sub_name`) VALUES (3, '计算机');
INSERT INTO `tb_subject` (`sub_id`, `sub_name`) VALUES (4, '操作系统');
INSERT INTO `tb_subject` (`sub_id`, `sub_name`) VALUES (5, '数据库');
COMMIT;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL COMMENT '主键ID（全局唯一）',
  `user_name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `deleted` bit(1) DEFAULT b'0' COMMENT '逻辑删除（0:未删除；1:已删除）',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  KEY `dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='书籍';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
BEGIN;
INSERT INTO `tb_user` (`user_id`, `user_name`, `age`, `email`, `dept_id`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (1, 'Jone', 1, 'ab@c.c', 10, b'0', 0, NULL, '2021-05-24 15:12:01');
INSERT INTO `tb_user` (`user_id`, `user_name`, `age`, `email`, `dept_id`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (2, 'Jack', 3, 'test2@baomidou.com', 11, b'0', 0, NULL, '2021-05-24 15:12:04');
INSERT INTO `tb_user` (`user_id`, `user_name`, `age`, `email`, `dept_id`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (3, 'Billie', 2, 'test5@baomidou.com', 12, b'0', 0, NULL, '2021-10-21 10:51:24');
INSERT INTO `tb_user` (`user_id`, `user_name`, `age`, `email`, `dept_id`, `deleted`, `version`, `gmt_create`, `gmt_modified`) VALUES (4, 'didi', 12, 'test@qq.com', 12, b'0', 0, '2021-06-05 19:22:46', '2021-10-21 14:38:26');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
