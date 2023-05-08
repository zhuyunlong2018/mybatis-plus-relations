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

 Date: 08/05/2023 19:25:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for r_dept
-- ----------------------------
DROP TABLE IF EXISTS `r_dept`;
CREATE TABLE `r_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID（全局唯一）',
  `name` varchar(30) DEFAULT NULL COMMENT '部门名称',
  `tel` varchar(50) DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Records of r_dept
-- ----------------------------
BEGIN;
INSERT INTO `r_dept` (`id`, `name`, `tel`) VALUES (1, '开发部', '88886666');
INSERT INTO `r_dept` (`id`, `name`, `tel`) VALUES (2, '客服部', '80802121');
INSERT INTO `r_dept` (`id`, `name`, `tel`) VALUES (3, '业务部', '23231212');
INSERT INTO `r_dept` (`id`, `name`, `tel`) VALUES (4, '安保部', '7116201');
COMMIT;

-- ----------------------------
-- Table structure for r_skill
-- ----------------------------
DROP TABLE IF EXISTS `r_skill`;
CREATE TABLE `r_skill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '技能ID',
  `name` varchar(255) DEFAULT NULL COMMENT '技能名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='技能表';

-- ----------------------------
-- Records of r_skill
-- ----------------------------
BEGIN;
INSERT INTO `r_skill` (`id`, `name`) VALUES (1, 'java技能');
INSERT INTO `r_skill` (`id`, `name`) VALUES (2, '数据库设计');
INSERT INTO `r_skill` (`id`, `name`) VALUES (3, '瞎聊技能');
INSERT INTO `r_skill` (`id`, `name`) VALUES (4, '喝酒');
INSERT INTO `r_skill` (`id`, `name`) VALUES (5, '说唱');
INSERT INTO `r_skill` (`id`, `name`) VALUES (6, '沟通技能');
INSERT INTO `r_skill` (`id`, `name`) VALUES (7, '基础武技');
INSERT INTO `r_skill` (`id`, `name`) VALUES (8, '看大门');
COMMIT;

-- ----------------------------
-- Table structure for r_user
-- ----------------------------
DROP TABLE IF EXISTS `r_user`;
CREATE TABLE `r_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID（全局唯一）',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dept_id` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='书籍';

-- ----------------------------
-- Records of r_user
-- ----------------------------
BEGIN;
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (1, '王某某(王开发)', 20, 1);
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (2, '李二哈(李开发)', 35, 1);
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (3, '张麻子(客服小张)', 26, 2);
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (4, '陈大圆(业务经理)', 34, 3);
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (5, '赵钱钱(业务赵总)', 37, 3);
INSERT INTO `r_user` (`id`, `name`, `age`, `dept_id`) VALUES (6, '胡虎(保安队长)', 55, 4);
COMMIT;

-- ----------------------------
-- Table structure for r_user_skill_relation
-- ----------------------------
DROP TABLE IF EXISTS `r_user_skill_relation`;
CREATE TABLE `r_user_skill_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '学号ID',
  `skill_id` int(11) DEFAULT NULL COMMENT '学生ID',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `skill_id` (`skill_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户和技能关系表';

-- ----------------------------
-- Records of r_user_skill_relation
-- ----------------------------
BEGIN;
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (1, 1, 1, 88);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (2, 1, 2, 67);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (3, 2, 1, 82);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (4, 2, 2, 14);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (5, 3, 3, 100);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (6, 4, 4, 60);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (7, 4, 5, 89);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (8, 5, 4, 80);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (9, 5, 5, 98);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (10, 5, 6, 90);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (11, 6, 7, 68);
INSERT INTO `r_user_skill_relation` (`id`, `user_id`, `skill_id`, `score`) VALUES (12, 6, 8, 100);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
