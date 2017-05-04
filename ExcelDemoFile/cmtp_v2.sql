/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50130
Source Host           : localhost:3306
Source Database       : cmrp

Target Server Type    : MYSQL
Target Server Version : 50130
File Encoding         : 65001

Date: 2017-05-04 21:47:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `a_agent`
-- ----------------------------
DROP TABLE IF EXISTS `a_agent`;
CREATE TABLE `a_agent` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `renew` decimal(10,2) DEFAULT NULL,
  `type` varchar(5) DEFAULT NULL,
  `creater` varchar(50) DEFAULT NULL,
  `creatdate` varchar(20) DEFAULT NULL,
  `parentId` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `a_role`
-- ----------------------------
DROP TABLE IF EXISTS `a_role`;
CREATE TABLE `a_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `roleCode` varchar(20) DEFAULT NULL,
  `roleName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of a_role
-- ----------------------------
INSERT INTO `a_role` VALUES ('1', 'admin', '管理员');
INSERT INTO `a_role` VALUES ('2', 'agent', '代理商');

-- ----------------------------
-- Table structure for `a_user`
-- ----------------------------
DROP TABLE IF EXISTS `a_user`;
CREATE TABLE `a_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userNo` varchar(50) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `pwd` varchar(20) DEFAULT NULL,
  `roleid` int(10) NOT NULL,
  `agentid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of a_user
-- ----------------------------


-- ----------------------------
-- Table structure for `card_agent`
-- ----------------------------
DROP TABLE IF EXISTS `card_agent`;
CREATE TABLE `card_agent` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `iccid` varchar(100) DEFAULT NULL,
  `agentid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=57022 DEFAULT CHARSET=utf8;

