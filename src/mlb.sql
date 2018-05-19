/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost
 Source Database       : cmtp

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : utf-8

 Date: 05/19/2018 17:34:11 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_mlb_record`
-- ----------------------------
DROP TABLE IF EXISTS `t_mlb_record`;
CREATE TABLE `t_mlb_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wxOrderId` varchar(20) DEFAULT NULL,
  `OrderSign` varchar(50) DEFAULT NULL COMMENT '订单号',
  `wxOrderNo` varchar(50) DEFAULT NULL COMMENT '微信交易单号',
  `Amount` double(10,3) DEFAULT NULL COMMENT '金额',
  `ICCID` varchar(50) DEFAULT NULL COMMENT 'ICCID',
  `SIM` varchar(50) DEFAULT NULL COMMENT 'SIM',
  `IMSI` varchar(50) DEFAULT NULL COMMENT 'ISMI',
  `IMEI` varchar(50) DEFAULT NULL,
  `PackageName` varchar(50) DEFAULT NULL COMMENT '续费套餐',
  `PayState` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `PayTime` varchar(50) DEFAULT NULL COMMENT '支付时间',
  `CreateTime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `PayMenter` varchar(50) DEFAULT NULL COMMENT '支付账户',
  `isToActiveOrder` varchar(50) DEFAULT NULL COMMENT '续费类型',
  `isPush` varchar(10) DEFAULT NULL COMMENT '是否推送',
  `holdName` varchar(10) DEFAULT NULL COMMENT '所属用户',
  `oldPackageName` varchar(20) DEFAULT NULL COMMENT '基础套餐',
  `isFirst` varchar(5) DEFAULT NULL COMMENT '首次支付',
  `simState` varchar(10) DEFAULT NULL COMMENT '卡状态',
  `activeTimespan` varchar(10) DEFAULT NULL COMMENT '距首次激活',
  `payEEName` varchar(20) DEFAULT NULL COMMENT '商户',
  `accessEEName` varchar(20) DEFAULT NULL COMMENT '接入方',
  `platformType` varchar(20) DEFAULT NULL,
  `RenewalsStatus` varchar(20) DEFAULT NULL COMMENT '续费状态',
  `RenewalsStatusMsg` varchar(60) DEFAULT NULL COMMENT '失败原因',
  `SimFromType` varchar(10) DEFAULT NULL,
  `Tag` varchar(10) DEFAULT NULL COMMENT '标签',
  `TradeType` varchar(10) DEFAULT NULL COMMENT '支付方式',
  `SourceType` varchar(10) DEFAULT NULL COMMENT '卡源',
  `Receivables` varchar(10) DEFAULT NULL,
  `isExpireRenewals` varchar(10) DEFAULT NULL COMMENT '到期续费',
  `MonthUsageData` varchar(10) DEFAULT NULL COMMENT '当月用量',
  `recordTime` varchar(255) DEFAULT NULL COMMENT '系统执行时间',
  PRIMARY KEY (`id`),
  KEY `iccid` (`ICCID`)
) ENGINE=InnoDB AUTO_INCREMENT=1078 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_mlb_temp`
-- ----------------------------
DROP TABLE IF EXISTS `t_mlb_temp`;
CREATE TABLE `t_mlb_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wxOrderId` varchar(20) DEFAULT NULL,
  `OrderSign` varchar(50) DEFAULT NULL COMMENT '订单号',
  `wxOrderNo` varchar(50) DEFAULT NULL COMMENT '微信交易单号',
  `Amount` double(10,3) DEFAULT NULL COMMENT '金额',
  `ICCID` varchar(50) DEFAULT NULL COMMENT 'ICCID',
  `SIM` varchar(50) DEFAULT NULL COMMENT 'SIM',
  `IMSI` varchar(50) DEFAULT NULL COMMENT 'ISMI',
  `IMEI` varchar(50) DEFAULT NULL,
  `PackageName` varchar(50) DEFAULT NULL COMMENT '续费套餐',
  `PayState` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `PayTime` varchar(50) DEFAULT NULL COMMENT '支付时间',
  `CreateTime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `PayMenter` varchar(50) DEFAULT NULL COMMENT '支付账户',
  `isToActiveOrder` varchar(50) DEFAULT NULL COMMENT '续费类型',
  `isPush` varchar(10) DEFAULT NULL COMMENT '是否推送',
  `holdName` varchar(10) DEFAULT NULL COMMENT '所属用户',
  `oldPackageName` varchar(20) DEFAULT NULL COMMENT '基础套餐',
  `isFirst` varchar(5) DEFAULT NULL COMMENT '首次支付',
  `simState` varchar(10) DEFAULT NULL COMMENT '卡状态',
  `activeTimespan` varchar(10) DEFAULT NULL COMMENT '距首次激活',
  `payEEName` varchar(20) DEFAULT NULL COMMENT '商户',
  `accessEEName` varchar(20) DEFAULT NULL COMMENT '接入方',
  `platformType` varchar(20) DEFAULT NULL,
  `RenewalsStatus` varchar(20) DEFAULT NULL COMMENT '续费状态',
  `RenewalsStatusMsg` varchar(60) DEFAULT NULL COMMENT '失败原因',
  `SimFromType` varchar(10) DEFAULT NULL,
  `Tag` varchar(10) DEFAULT NULL COMMENT '标签',
  `TradeType` varchar(10) DEFAULT NULL COMMENT '支付方式',
  `SourceType` varchar(10) DEFAULT NULL COMMENT '卡源',
  `Receivables` varchar(10) DEFAULT NULL,
  `isExpireRenewals` varchar(10) DEFAULT NULL COMMENT '到期续费',
  `MonthUsageData` varchar(10) DEFAULT NULL COMMENT '当月用量',
  `recordTime` varchar(255) DEFAULT NULL COMMENT '系统执行时间',
  PRIMARY KEY (`id`),
  KEY `iccid` (`ICCID`)
) ENGINE=InnoDB AUTO_INCREMENT=941 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_task_point`
-- ----------------------------
DROP TABLE IF EXISTS `t_task_point`;
CREATE TABLE `t_task_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pointTime` varchar(30) DEFAULT NULL,
  `endTime` varchar(30) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
