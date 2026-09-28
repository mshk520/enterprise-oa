-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: web_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `car_access_record`
--

DROP TABLE IF EXISTS `car_access_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_access_record` (
  `access_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `plate_number` varchar(20) NOT NULL COMMENT '车牌号',
  `driver_name` varchar(50) DEFAULT NULL COMMENT '司机姓名',
  `driver_name_en` varchar(50) DEFAULT NULL COMMENT '司机姓名(英文)',
  `out_time` datetime DEFAULT NULL COMMENT '出厂时间',
  `in_time` datetime DEFAULT NULL COMMENT '回厂时间',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '登记人',
  `operator_name_en` varchar(50) DEFAULT NULL COMMENT '登记人(英文)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记 0存在 2删除',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `apply_id` bigint(20) DEFAULT NULL COMMENT '关联申请单ID',
  `vehicle_id` bigint(20) DEFAULT NULL COMMENT '关联车辆ID',
  PRIMARY KEY (`access_id`) USING BTREE,
  KEY `idx_plate_number` (`plate_number`) USING BTREE,
  KEY `idx_out_time` (`out_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='车辆出入记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_access_record`
--

LOCK TABLES `car_access_record` WRITE;
/*!40000 ALTER TABLE `car_access_record` DISABLE KEYS */;
INSERT INTO `car_access_record` VALUES (1,'粤A00012','王建国',NULL,'2026-08-25 14:28:52','2026-08-25 14:28:54','admin',NULL,'2','admin','2026-08-25 14:28:51','admin','2026-08-25 14:28:54',NULL,8,12),(2,'粤A00011','李志强',NULL,'2026-08-25 16:10:25','2026-08-25 16:10:26','admin',NULL,'2','admin','2026-08-25 16:10:24','admin','2026-08-26 08:37:25',NULL,11,11),(3,'粤A00010','李志强',NULL,'2026-08-26 08:38:05','2026-08-26 08:38:07','admin',NULL,'0','admin','2026-08-26 08:38:04','admin','2026-08-26 08:38:06',NULL,1,10),(4,'粤A00012','李志强',NULL,'2026-08-26 09:48:54','2026-08-26 09:48:56','test陈思凡',NULL,'0','test陈思凡','2026-08-26 09:48:54','test陈思凡','2026-08-26 09:48:56',NULL,3,12),(5,'粤A00012','李志强',NULL,'2026-08-26 09:57:42','2026-08-26 09:57:45','test陈思凡',NULL,'0','test陈思凡','2026-08-26 09:57:41','test陈思凡','2026-08-26 09:57:44',NULL,4,12),(6,'粤A00013','李志强',NULL,'2026-08-26 10:00:06','2026-08-26 10:00:09','test陈思凡',NULL,'0','test陈思凡','2026-08-26 10:00:06','test陈思凡','2026-08-26 10:00:09',NULL,5,13),(7,'粤A00013','李志强',NULL,'2026-08-26 10:24:43','2026-08-26 10:24:46','test陈思凡',NULL,'0','test陈思凡','2026-08-26 10:24:43','test陈思凡','2026-08-26 10:24:45',NULL,1,13),(8,'粤A00008','张伟民',NULL,'2026-08-26 10:29:36','2026-08-26 10:29:38','admin',NULL,'0','admin','2026-08-26 10:29:35','admin','2026-08-26 10:29:37',NULL,2,8);
/*!40000 ALTER TABLE `car_access_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_apply`
--

DROP TABLE IF EXISTS `car_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_apply` (
  `apply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用车申请ID',
  `apply_no` varchar(30) DEFAULT NULL COMMENT '申请单号',
  `applicant_name` varchar(50) NOT NULL COMMENT '申请人姓名',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '申请部门',
  `apply_date` date DEFAULT NULL COMMENT '申请日期',
  `start_place` varchar(100) DEFAULT NULL COMMENT '出发地',
  `end_place` varchar(100) DEFAULT NULL COMMENT '目的地',
  `departure_time` datetime DEFAULT NULL COMMENT '出发时间',
  `return_time` datetime DEFAULT NULL COMMENT '预计回归时间',
  `return_time_tbd` char(1) DEFAULT 'N' COMMENT '回归时间待定 Y是 N否',
  `trip_type` char(1) DEFAULT '1' COMMENT '单程双程 1单程 2双程',
  `use_type` char(1) DEFAULT '1' COMMENT '用车性质 1因公 2私人',
  `purpose_type` varchar(200) DEFAULT NULL COMMENT '用途类型 逗号分隔 goods/people/pickup/business/meal/other',
  `driver_accompany` char(1) DEFAULT 'N' COMMENT '用餐是否司机陪同 Y是 N否',
  `other_purpose` varchar(200) DEFAULT NULL COMMENT '其他用途说明',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `purpose` varchar(200) DEFAULT NULL COMMENT '用车事由',
  `vehicle_id` bigint(20) DEFAULT NULL COMMENT '车辆ID',
  `driver_id` bigint(20) DEFAULT NULL COMMENT '司机ID',
  `vehicle_type` char(1) DEFAULT NULL COMMENT '车辆类型 1=公司车辆 2=街车车辆',
  `external_plate_number` varchar(50) DEFAULT NULL COMMENT '街车车辆车牌号',
  `external_driver_phone` varchar(20) DEFAULT NULL COMMENT '街车司机联系方式',
  `status` char(1) DEFAULT '0' COMMENT '状态:0待审批 1已批准 2已拒绝 3已结束',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审批意见',
  `audit_by` varchar(64) DEFAULT NULL COMMENT '审批人',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `hidden_by` bigint(20) DEFAULT NULL COMMENT '隐藏该记录的用户ID',
  `is_urgent` char(1) DEFAULT '0' COMMENT '是否紧急待补签（0否 1是）',
  PRIMARY KEY (`apply_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_departure_time` (`departure_time`) USING BTREE,
  KEY `idx_use_type` (`use_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用车申请信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_apply`
--

LOCK TABLES `car_apply` WRITE;
/*!40000 ALTER TABLE `car_apply` DISABLE KEYS */;
INSERT INTO `car_apply` VALUES (1,'YY260826102443661','test陈思凡',NULL,'2026-08-26',NULL,NULL,'2026-08-26 10:24:43',NULL,'N','1','1',NULL,'N',NULL,NULL,NULL,13,NULL,NULL,NULL,NULL,'3',NULL,NULL,NULL,NULL,'test陈思凡','2026-08-26 10:24:43','','2026-08-26 10:24:45','0',NULL,'1'),(2,'YC202608267113','超级管理员','超级管理员',NULL,'公司总部','客户工厂','2026-08-26 10:29:00',NULL,'N','1','1','people','N',NULL,'',NULL,8,3,'1','','','3','','admin','2026-08-26 10:29:29',NULL,'admin','2026-08-26 10:29:24','','2026-08-26 10:29:37','0',NULL,'0');
/*!40000 ALTER TABLE `car_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_approval`
--

DROP TABLE IF EXISTS `car_approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_approval` (
  `approval_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `approver_id` bigint(20) NOT NULL,
  `approve_level` char(1) DEFAULT '1' COMMENT '1部门审批 2车管员派车',
  `approve_status` char(1) DEFAULT '0' COMMENT '0待审批 1通过 2驳回',
  `opinion` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`approval_id`) USING BTREE,
  KEY `idx_application_id` (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='审批记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_approval`
--

LOCK TABLES `car_approval` WRITE;
/*!40000 ALTER TABLE `car_approval` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_approval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_driver`
--

DROP TABLE IF EXISTS `car_driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_driver` (
  `driver_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `driver_name` varchar(50) NOT NULL COMMENT '司机姓名',
  `driver_name_en` varchar(50) DEFAULT NULL COMMENT '司机姓名(英文)',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` char(1) DEFAULT '0' COMMENT '0在职 1休假',
  `remark` varchar(200) DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='司机信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_driver`
--

LOCK TABLES `car_driver` WRITE;
/*!40000 ALTER TABLE `car_driver` DISABLE KEYS */;
INSERT INTO `car_driver` VALUES (1,'王建国','Wang JG','13800000001','0',NULL,'admin','2026-08-25 17:26:00','',NULL,'0'),(2,'李志强','Li ZQ','13800000002','0',NULL,'admin','2026-08-25 17:26:15','',NULL,'0'),(3,'张伟民','Zhang WM','13800000003','0',NULL,'admin','2026-08-25 17:26:26','',NULL,'0');
/*!40000 ALTER TABLE `car_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_expense`
--

DROP TABLE IF EXISTS `car_expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_expense` (
  `expense_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `plate_number` varchar(50) NOT NULL COMMENT '车牌号',
  `apply_id` bigint(20) DEFAULT NULL COMMENT '关联用车申请ID',
  `use_type` char(1) DEFAULT NULL COMMENT '用车性质 1=因公用車 2=私人用車',
  `fuel_amount` decimal(10,2) DEFAULT NULL COMMENT '加油金额',
  `toll_fee` decimal(10,2) DEFAULT NULL COMMENT '过路及粤通卡',
  `maintenance_fee` decimal(10,2) DEFAULT NULL COMMENT '维修费',
  `oil_change` decimal(10,2) DEFAULT NULL COMMENT '换机油',
  `tire_change` decimal(10,2) DEFAULT NULL COMMENT '换车胎',
  `brake_change` decimal(10,2) DEFAULT NULL COMMENT '换刹车片',
  `insurance_tax` decimal(10,2) DEFAULT NULL COMMENT '保险及车船税',
  `phone_fee` decimal(10,2) DEFAULT NULL COMMENT '电话费',
  `parking_fee` decimal(10,2) DEFAULT NULL COMMENT '停车费',
  `hotel_fee` decimal(10,2) DEFAULT NULL COMMENT '住宿费',
  `meal_fee` decimal(10,2) DEFAULT NULL COMMENT '餐费',
  `annual_inspection` decimal(10,2) DEFAULT NULL COMMENT '年审',
  `mileage` decimal(10,2) DEFAULT NULL COMMENT '行驶里程',
  `fuel_volume` decimal(10,2) DEFAULT NULL COMMENT '入油数（升）',
  `fuel_per_km` decimal(10,2) DEFAULT NULL COMMENT '耗油金额/公里',
  `fuel_per_100km` decimal(10,2) DEFAULT NULL COMMENT '耗油/百公里',
  `other_fee` decimal(10,2) DEFAULT NULL COMMENT '其它费用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志 0=正常 2=已删除',
  `access_id` bigint(20) DEFAULT NULL COMMENT '关联出入记录ID',
  PRIMARY KEY (`expense_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='车辆费用记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_expense`
--

LOCK TABLES `car_expense` WRITE;
/*!40000 ALTER TABLE `car_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_gate_log`
--

DROP TABLE IF EXISTS `car_gate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_gate_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) DEFAULT NULL,
  `plate_number` varchar(20) NOT NULL COMMENT '车牌号',
  `driver_name` varchar(50) DEFAULT NULL COMMENT '司机姓名',
  `gate_type` char(1) NOT NULL COMMENT '1出厂 2回厂',
  `gate_time` datetime NOT NULL COMMENT '出厂/回厂时间',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '保安姓名',
  `remark` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `idx_plate_number` (`plate_number`) USING BTREE,
  KEY `idx_gate_time` (`gate_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='保安部出入记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_gate_log`
--

LOCK TABLES `car_gate_log` WRITE;
/*!40000 ALTER TABLE `car_gate_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_gate_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_notification`
--

DROP TABLE IF EXISTS `car_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_notification` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '接收人',
  `application_id` bigint(20) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `is_read` tinyint(4) DEFAULT 0 COMMENT '0未读 1已读',
  `channel` char(1) DEFAULT '1' COMMENT '1站内消息 2邮件',
  `send_time` datetime DEFAULT NULL,
  `read_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`notification_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_is_read` (`is_read`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_notification`
--

LOCK TABLES `car_notification` WRITE;
/*!40000 ALTER TABLE `car_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_passenger`
--

DROP TABLE IF EXISTS `car_passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_passenger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_application_id` (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='随行人员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_passenger`
--

LOCK TABLES `car_passenger` WRITE;
/*!40000 ALTER TABLE `car_passenger` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_report_config`
--

DROP TABLE IF EXISTS `car_report_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_report_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_name` varchar(100) NOT NULL COMMENT '报表名称',
  `sql_template` text DEFAULT NULL COMMENT 'SQL模板',
  `columns` text DEFAULT NULL COMMENT '列配置JSON',
  `enabled` tinyint(4) DEFAULT 1,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='报表配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_report_config`
--

LOCK TABLES `car_report_config` WRITE;
/*!40000 ALTER TABLE `car_report_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_report_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_vehicle`
--

DROP TABLE IF EXISTS `car_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_vehicle` (
  `vehicle_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
  `plate_number` varchar(20) NOT NULL COMMENT '车牌号',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `brand_en` varchar(50) DEFAULT NULL COMMENT '品牌(英文)',
  `color` varchar(50) DEFAULT NULL COMMENT '颜色',
  `color_en` varchar(20) DEFAULT NULL COMMENT '颜色(英文)',
  `seat_count` int(11) DEFAULT 5 COMMENT '座位数',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '归属部门ID',
  `status` char(1) DEFAULT '0' COMMENT '0空闲 1已派出 2维修中 3已报废',
  `total_mileage` decimal(12,2) DEFAULT 0.00 COMMENT '总里程',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`vehicle_id`) USING BTREE,
  UNIQUE KEY `uk_plate_number` (`plate_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='车辆信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_vehicle`
--

LOCK TABLES `car_vehicle` WRITE;
/*!40000 ALTER TABLE `car_vehicle` DISABLE KEYS */;
INSERT INTO `car_vehicle` VALUES (4,'粤A00004','廣汽傳祺E8',NULL,'黑色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-11 16:40:18','','2026-08-21 11:19:44','0'),(5,'粤A00005','廣汽傳祺E8',NULL,'黑色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-12 11:09:43','admin','2026-08-25 10:26:06','0'),(6,'粤A00006','本田奧德賽',NULL,'藍色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 10:58:22','','2026-08-21 11:19:38','0'),(7,'粤A00007','本田奧德賽',NULL,'藍色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 10:58:50','admin','2026-08-24 16:35:42','0'),(8,'粤A00008','寶駿',NULL,'灰色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 10:59:30','admin','2026-08-26 10:29:37','0'),(9,'粤A00009','本田繽智',NULL,'黑色',NULL,5,10,'0',0.00,NULL,NULL,'2026-08-21 11:00:46','admin','2026-08-24 16:48:09','0'),(10,'粤A00010','本田奧德賽',NULL,'藍色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 11:01:10','admin','2026-08-26 08:38:06','0'),(11,'粤A00011','廣汽傳祺E9',NULL,'黑色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 11:02:29','admin','2026-08-25 16:10:26','0'),(12,'粤A00012','本田艾力紳',NULL,'棕色',NULL,7,10,'0',0.00,NULL,NULL,'2026-08-21 11:03:11','test陈思凡','2026-08-26 09:57:44','0'),(13,'粤A00013','慶鈴牌五十鈴貨車',NULL,'白色',NULL,2,10,'0',0.00,NULL,NULL,'2026-08-21 11:04:05','test陈思凡','2026-08-26 10:24:45','0');
/*!40000 ALTER TABLE `car_vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '編號',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名稱',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '關聯子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表關聯的外鍵名',
  `class_name` varchar(100) DEFAULT '' COMMENT '實體類名稱',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud單表操作 tree樹表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板類型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路徑',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模組名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成業務名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功術名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功術名作者',
  `form_col_num` int(11) DEFAULT 1 COMMENT '表單佈局（單列 雙列 三列）',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代碼方式（0zip壓縮包 1自定義路徑）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路徑（不填默認項目路徑）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成選項',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='代碼生成業務表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '編號',
  `table_id` bigint(20) DEFAULT NULL COMMENT '歸屬表編號',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名稱',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列類型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA類型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主鍵（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否為插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否編輯字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查詢字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查詢方式（等於、不等於、大於、小於、範圍）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '顯示類型（文本框、文本域、下拉框、複選框、單選框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典類型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='代碼生成業務表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_booking`
--

DROP TABLE IF EXISTS `mtg_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_booking` (
  `booking_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  `subject` varchar(200) NOT NULL COMMENT '会议主题',
  `booker_id` bigint(20) DEFAULT NULL,
  `dept_id` bigint(20) DEFAULT NULL COMMENT '预约人所在部门ID（冗余，便于统计）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `booking_status` char(1) DEFAULT '0' COMMENT '状态（0草稿 1待审批 2已通过 3已驳回 4已取消 5进行中 6已结束）',
  `is_periodic` tinyint(4) DEFAULT 0 COMMENT '是否周期预约（0否 1是）',
  `periodic_type` char(1) DEFAULT NULL COMMENT '周期类型（D:日 W:周 M:月）',
  `periodic_end_date` date DEFAULT NULL COMMENT '周期结束日期',
  `parent_booking_id` bigint(20) DEFAULT NULL COMMENT '父预约ID（周期预约的主记录，自关联）',
  `periodic_rule` varchar(500) DEFAULT NULL COMMENT '周期规则JSON（如每周一、三、五）',
  `cancel_reason` varchar(200) DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记（0存在 2删除）',
  `booking_type` varchar(10) DEFAULT 'hour' COMMENT '预约类型 hour-按小时 day-按天',
  `booking_date` date DEFAULT NULL COMMENT '预约日期',
  `attendees` int(11) DEFAULT 1 COMMENT '参会人数',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `meeting_link` varchar(500) DEFAULT NULL COMMENT '会议链接',
  `meeting_password` varchar(100) DEFAULT NULL COMMENT '会议密码',
  `fixed_id` bigint(20) DEFAULT NULL COMMENT '固定预约ID',
  `service_items` varchar(500) DEFAULT NULL COMMENT '服务项目(茶水/矿泉水等,逗号分隔)',
  `hidden_by` bigint(20) DEFAULT NULL COMMENT '隐藏该记录的用户ID',
  PRIMARY KEY (`booking_id`) USING BTREE,
  KEY `idx_room_time` (`room_id`,`start_time`,`end_time`) USING BTREE,
  KEY `idx_booker_id` (`booker_id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_status` (`booking_status`) USING BTREE,
  KEY `idx_start_time` (`start_time`) USING BTREE,
  KEY `idx_parent_booking` (`parent_booking_id`) USING BTREE,
  KEY `idx_del_flag` (`del_flag`) USING BTREE,
  KEY `idx_fixed_id` (`fixed_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='预约记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_booking`
--

LOCK TABLES `mtg_booking` WRITE;
/*!40000 ALTER TABLE `mtg_booking` DISABLE KEYS */;
INSERT INTO `mtg_booking` VALUES (3,8,'测试缓存1111',1,103,'2026-08-25 09:30:00','2026-08-25 10:00:00','0',NULL,NULL,NULL,NULL,NULL,NULL,'','admin','2026-08-25 16:09:30','',NULL,'2','hour','2026-08-25',1,'','1111','12345',NULL,'如有其他需求請聯繫總經辦',NULL),(4,8,'产品迭代评审',1,103,'2026-09-28 09:30:00','2026-09-28 10:30:00','1',0,NULL,NULL,NULL,NULL,NULL,'','admin','2026-09-28 14:35:30','',NULL,'0','hour','2026-09-28',6,NULL,NULL,NULL,NULL,NULL,NULL),(5,12,'需求澄清会',1,103,'2026-09-28 14:00:00','2026-09-28 15:30:00','1',0,NULL,NULL,NULL,NULL,NULL,'','admin','2026-09-28 14:35:30','',NULL,'0','hour','2026-09-28',8,NULL,NULL,NULL,NULL,NULL,NULL),(6,13,'季度经营分析',1,103,'2026-09-29 10:00:00','2026-09-29 12:00:00','0',0,NULL,NULL,NULL,NULL,NULL,'','admin','2026-09-28 14:35:30','',NULL,'0','hour','2026-09-29',12,NULL,NULL,NULL,NULL,NULL,NULL),(7,8,'跨部门同步',1,103,'2026-09-30 15:00:00','2026-09-30 16:00:00','1',0,NULL,NULL,NULL,NULL,NULL,'','admin','2026-09-28 14:35:30','',NULL,'0','hour','2026-09-30',5,NULL,NULL,NULL,NULL,NULL,NULL),(8,12,'供应商月度沟通',1,103,'2026-10-01 09:00:00','2026-10-01 10:00:00','1',0,NULL,NULL,NULL,NULL,NULL,'','admin','2026-09-28 14:35:30','',NULL,'0','hour','2026-10-01',4,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `mtg_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_booking_attendee`
--

DROP TABLE IF EXISTS `mtg_booking_attendee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_booking_attendee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `booking_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `is_external` tinyint(4) DEFAULT 0,
  `external_name` varchar(50) DEFAULT NULL,
  `external_email` varchar(100) DEFAULT NULL,
  `external_phone` varchar(20) DEFAULT NULL,
  `status` char(1) DEFAULT '0',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_booking_attendee`
--

LOCK TABLES `mtg_booking_attendee` WRITE;
/*!40000 ALTER TABLE `mtg_booking_attendee` DISABLE KEYS */;
/*!40000 ALTER TABLE `mtg_booking_attendee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_device_dict`
--

DROP TABLE IF EXISTS `mtg_device_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_device_dict` (
  `device_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `device_name` varchar(50) NOT NULL COMMENT '设备名称（投影仪/白板/视频会议/音响等）',
  `device_name_en` varchar(100) DEFAULT NULL COMMENT '设备名称(英文)',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='设备字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_device_dict`
--

LOCK TABLES `mtg_device_dict` WRITE;
/*!40000 ALTER TABLE `mtg_device_dict` DISABLE KEYS */;
INSERT INTO `mtg_device_dict` VALUES (9,'視像','Video',NULL,0,'0','admin','2026-08-03 12:17:50','',NULL,NULL),(10,'電腦','PC Set',NULL,1,'0','admin','2026-08-05 17:11:53','admin','2026-08-25 11:05:40',NULL),(11,'攝像頭','Camera',NULL,3,'0','admin','2026-08-05 17:12:24','admin','2026-08-25 11:06:12',NULL),(12,'音響','Audio',NULL,2,'0','admin','2026-08-05 17:12:48','admin','2026-08-25 11:06:47',NULL),(13,'白板','Whiteboard',NULL,4,'0','admin','2026-08-05 17:13:31','admin','2026-08-25 11:07:32',NULL);
/*!40000 ALTER TABLE `mtg_device_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_fixed_booking`
--

DROP TABLE IF EXISTS `mtg_fixed_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_fixed_booking` (
  `fixed_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '固定预约ID',
  `booking_title` varchar(50) NOT NULL COMMENT '会议主题',
  `start_time` varchar(5) NOT NULL COMMENT '开始时间(HH:mm)',
  `end_time` varchar(5) NOT NULL COMMENT '结束时间(HH:mm)',
  `recurrence_type` varchar(10) NOT NULL COMMENT '重复类型(daily/weekly/monthly)',
  `recurrence_day` int(11) DEFAULT NULL COMMENT '重复日(周1-7/月1-31)',
  `start_date` date NOT NULL COMMENT '生效开始日期',
  `end_date` date NOT NULL COMMENT '生效结束日期',
  `booker_id` bigint(20) DEFAULT NULL COMMENT '预约人ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `attendees` int(11) DEFAULT 1 COMMENT '参会人数',
  `contact_phone` varchar(11) DEFAULT '' COMMENT '联系电话',
  `meeting_link` varchar(500) DEFAULT '' COMMENT '会议链接',
  `meeting_password` varchar(100) DEFAULT '' COMMENT '会议密码',
  `service_items` varchar(500) DEFAULT '' COMMENT '服务项目',
  `status` char(1) DEFAULT '0' COMMENT '状态(0启用 1停用)',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  PRIMARY KEY (`fixed_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='固定预约规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_fixed_booking`
--

LOCK TABLES `mtg_fixed_booking` WRITE;
/*!40000 ALTER TABLE `mtg_fixed_booking` DISABLE KEYS */;
INSERT INTO `mtg_fixed_booking` VALUES (1,'测试月报','08:00','10:00','monthly',1,'2026-07-29','2027-08-20',1,103,1,'1','1','1','矿泉水,茶水,如有其他需求请直接联系总经办','1','admin','2026-07-28 09:58:50','admin','2026-07-28 10:14:51',''),(2,'测试固定预约','10:00','12:00','monthly',1,'2027-07-30','2028-08-25',2,105,1,'1','1','1','茶水,矿泉水','1','ry','2026-07-28 11:40:24','ry','2026-07-28 14:05:05',''),(3,'测试固定预约','08:00','10:00','monthly',1,'2026-07-29','2026-09-24',2,105,1,'1','1','1','','1','ry','2026-07-28 11:41:32','ry','2026-07-28 14:18:31',''),(4,'测试固定','08:00','10:00','monthly',1,'2026-07-29','2027-08-11',2,105,1,'1','1','1','','1','ry','2026-07-28 14:19:01','admin','2026-08-14 16:21:28',''),(5,'测试','09:00','10:00','monthly',13,'2026-08-19','2026-09-30',1519,27,1,'1','1','1','','0','admin','2026-08-19 15:44:20','',NULL,'');
/*!40000 ALTER TABLE `mtg_fixed_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_fixed_room`
--

DROP TABLE IF EXISTS `mtg_fixed_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_fixed_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fixed_id` bigint(20) NOT NULL COMMENT '固定预约ID',
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_fixed_id` (`fixed_id`) USING BTREE,
  KEY `idx_room_id` (`room_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='固定预约-会议室关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_fixed_room`
--

LOCK TABLES `mtg_fixed_room` WRITE;
/*!40000 ALTER TABLE `mtg_fixed_room` DISABLE KEYS */;
INSERT INTO `mtg_fixed_room` VALUES (18,5,8);
/*!40000 ALTER TABLE `mtg_fixed_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_room`
--

DROP TABLE IF EXISTS `mtg_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_room` (
  `room_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  `room_name` varchar(100) NOT NULL COMMENT '会议室名称',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '所属部门ID（关联 sys_dept）',
  `floor` varchar(20) DEFAULT NULL COMMENT '楼层',
  `floor_en` varchar(100) DEFAULT NULL COMMENT '楼层(英文)',
  `location` varchar(200) DEFAULT NULL COMMENT '具体位置',
  `capacity` int(11) DEFAULT 0 COMMENT '容纳人数',
  `image_url` varchar(500) DEFAULT NULL COMMENT '图片URL',
  `open_time` time DEFAULT '08:00:00' COMMENT '开放开始时间',
  `close_time` time DEFAULT '18:00:00' COMMENT '开放结束时间',
  `buffer_time` int(11) DEFAULT 0 COMMENT '缓冲分钟数（会议前后不可预约时间）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0启用 1停用 2维护中）',
  `visibility_scope` varchar(20) DEFAULT 'ALL' COMMENT '可见范围: ALL=全部, DEPT=本部门, BRANCH=分公司',
  `need_approval` tinyint(4) DEFAULT 0 COMMENT '是否需要审批（0否 1是）',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记（0存在 2删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `room_name_en` varchar(100) DEFAULT NULL COMMENT '会议室名称（英文）',
  `location_en` varchar(200) DEFAULT NULL COMMENT '具体位置（英文）',
  `remark_en` varchar(500) DEFAULT NULL COMMENT '备注（英文）',
  PRIMARY KEY (`room_id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会议室信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_room`
--

LOCK TABLES `mtg_room` WRITE;
/*!40000 ALTER TABLE `mtg_room` DISABLE KEYS */;
INSERT INTO `mtg_room` VALUES (3,'三號會議室',103,'3F',NULL,'501',17,NULL,'08:00:00','18:00:00',10,'0','0',0,0,'admin','2026-07-27 11:25:23','admin','2026-07-27 16:49:05','0',NULL,'Meeting Room 3',NULL,NULL),(4,'五號會議室',106,'3F',NULL,'301',10,NULL,'08:00:00','18:00:00',0,'0','0',0,0,'admin','2026-07-27 11:31:15','admin','2026-08-03 08:54:07','0',NULL,'Meeting Room 5',NULL,NULL),(5,'一號會議室',1,'4FB座',NULL,NULL,10,NULL,'00:00:00','23:59:00',0,'0','0',0,0,'admin','2026-08-03 09:00:04','admin','2026-08-03 10:52:29','0',NULL,'Meeting Room A',NULL,NULL),(6,'二號會議室',1,'4FC座',NULL,NULL,10,NULL,'00:00:00','23:00:00',0,'0','0',0,0,'admin','2026-08-03 09:02:56','',NULL,'0',NULL,'Meeting Room B',NULL,NULL),(7,'三四號會議室',1,'4FC座',NULL,NULL,10,NULL,'00:00:00','23:00:00',0,'0','0',0,0,'admin','2026-08-03 09:03:30','',NULL,'0',NULL,'Meeting Room C',NULL,NULL),(8,'1號會議室',NULL,'B座4F','B Flat 4F',NULL,45,NULL,'08:00:00','18:00:00',0,'0','0',0,0,'admin','2026-08-03 12:04:21','admin','2026-08-25 10:00:02','0','','Meeting Room 1',NULL,NULL),(11,'二号会议室',NULL,NULL,NULL,NULL,10,NULL,'08:00:00','18:00:00',0,'0','0',0,0,'admin','2026-08-12 15:56:59','',NULL,'0',NULL,'Meeting Room D',NULL,NULL),(12,'2號會議室',NULL,'C座4F',NULL,NULL,16,NULL,'08:00:00','18:00:00',0,'0','0',0,0,'admin','2026-08-21 10:23:56','admin','2026-08-21 13:55:17','0',NULL,'Meeting Room 2',NULL,NULL),(13,'3/4號會議室',NULL,'C座4F',NULL,NULL,20,NULL,'08:00:00','18:00:00',0,'0','0',0,0,'admin','2026-08-21 10:25:13','admin','2026-08-21 13:55:33','0',NULL,'Meeting Room 3/4',NULL,NULL);
/*!40000 ALTER TABLE `mtg_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_room_device`
--

DROP TABLE IF EXISTS `mtg_room_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_room_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  `device_id` bigint(20) NOT NULL COMMENT '设备ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_room_device` (`room_id`,`device_id`) USING BTREE,
  KEY `idx_room_id` (`room_id`) USING BTREE,
  KEY `idx_device_id` (`device_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会议室设备关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_room_device`
--

LOCK TABLES `mtg_room_device` WRITE;
/*!40000 ALTER TABLE `mtg_room_device` DISABLE KEYS */;
INSERT INTO `mtg_room_device` VALUES (65,12,9,'admin',NULL),(66,12,10,'admin',NULL),(67,12,11,'admin',NULL),(68,12,12,'admin',NULL),(69,12,13,'admin',NULL),(70,13,11,'admin',NULL),(71,13,12,'admin',NULL),(77,8,9,'admin',NULL),(78,8,10,'admin',NULL),(79,8,11,'admin',NULL),(80,8,12,'admin',NULL),(81,8,13,'admin',NULL);
/*!40000 ALTER TABLE `mtg_room_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mtg_service_dict`
--

DROP TABLE IF EXISTS `mtg_service_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mtg_service_dict` (
  `service_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务ID',
  `service_name` varchar(50) NOT NULL COMMENT '服务名称',
  `service_name_en` varchar(200) DEFAULT NULL COMMENT '服务名称(英文)',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `email_notify` varchar(1) DEFAULT '0' COMMENT '是否邮件通知(0=否,1=是)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会议服务字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mtg_service_dict`
--

LOCK TABLES `mtg_service_dict` WRITE;
/*!40000 ALTER TABLE `mtg_service_dict` DISABLE KEYS */;
INSERT INTO `mtg_service_dict` VALUES (7,'茶水','tea',NULL,1,'0','0','admin','2026-08-03 12:08:01','admin','2026-08-20 15:38:48',NULL),(8,'礦泉水','mineral water',NULL,0,'0','0','admin','2026-08-03 12:08:25','admin','2026-08-07 14:34:34',NULL),(9,'如有其他需求請聯繫總經辦','For other requirements, please contact admin',NULL,2,'0','0','admin','2026-08-03 12:09:44','admin','2026-08-25 12:06:51',NULL);
/*!40000 ALTER TABLE `mtg_service_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob DEFAULT NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_blob_triggers`
--

LOCK TABLES `qrtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(13) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(13) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob DEFAULT NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_locks`
--

LOCK TABLES `qrtz_locks` WRITE;
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_paused_trigger_grps`
--

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(13) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(13) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_scheduler_state`
--

LOCK TABLES `qrtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(7) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(12) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(10) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simprop_triggers`
--

LOCK TABLES `qrtz_simprop_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(13) DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(13) DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(2) DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob DEFAULT NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '參數主鍵',
  `config_name` varchar(100) DEFAULT '' COMMENT '參數名稱',
  `config_key` varchar(100) DEFAULT '' COMMENT '參數鍵名',
  `config_value` varchar(500) DEFAULT '' COMMENT '參數鍵值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系統內置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='參數配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架頁-默認皮膚樣式名稱','sys.index.skinName','skin-blue','Y','admin','2026-07-24 10:28:55','',NULL,'藍色 skin-blue、綠色 skin-green、紫色 skin-purple、紅色 skin-red、黃色 skin-yellow'),(2,'用戶管理-賬號初始密碼','sys.user.initPassword','123456','Y','admin','2026-07-24 10:28:55','',NULL,'初始化密碼 123456'),(3,'主框架頁-側邊欄主題','sys.index.sideTheme','theme-dark','Y','admin','2026-07-24 10:28:55','',NULL,'深色主題theme-dark，淺色主題theme-light'),(4,'賬號自助-驗證碼開關','sys.account.captchaEnabled','0','Y','admin','2026-07-24 10:28:55','',NULL,'是否開啟驗證碼功能（true開啟，false關閉）'),(5,'賬號自助-是否開啟用戶註冊功能','sys.account.registerUser','false','Y','admin','2026-07-24 10:28:55','',NULL,'是否開啟註冊用戶功能（true開啟，false關閉）'),(6,'用戶登錄-黑名單列表','sys.login.blackIPList','','Y','admin','2026-07-24 10:28:55','',NULL,'設置登錄IP黑名單限制，多個匹配項以;分隔，支持匹配（*通配、網段）'),(7,'用戶管理-初始密碼修改策略','sys.account.initPasswordModify','1','Y','admin','2026-07-24 10:28:55','',NULL,'0：初始密碼修改策略關閉，沒有任何提示，1：提醒用戶，如果未修改初始密碼，則在登錄時就會提醒修改密碼對話框'),(8,'用戶管理-賬號密碼更新週期','sys.account.passwordValidateDays','0','Y','admin','2026-07-24 10:28:55','',NULL,'密碼更新週期（填寫數字，數據初始化值為0不限制，若修改必須為大於0小於365的正整數），如果超過這個週期登錄系統時，則在登錄時就會提醒修改密碼對話框'),(9,'用戶管理-密碼字符範圍','sys.account.chrtype','0','Y','admin','2026-07-24 10:28:55','',NULL,'默認任意字符範圍，0任意（密碼可以輸入任意字符），1數字（密碼只能為0-9數字），2英文字母（密碼只能為a-z和A-Z字母），3字母和數字（密碼必須包含字母，數字）,4字母數字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部門id',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父部門id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖級列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部門名稱',
  `dept_name_en` varchar(100) DEFAULT NULL,
  `order_num` int(11) DEFAULT 0 COMMENT '顯示順序',
  `leader` varchar(20) DEFAULT NULL COMMENT '負責人',
  `phone` varchar(11) DEFAULT NULL COMMENT '聯繫電話',
  `email` varchar(50) DEFAULT NULL COMMENT '郵箱',
  `status` char(1) DEFAULT '0' COMMENT '部門狀態（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '刪除標誌（0代表存在 2代表刪除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='部門表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (1,0,'0','远航科技有限公司',NULL,0,'admin','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-08-21 11:18:02'),(10,1,'0,1','远航科技(深圳)有限公司',NULL,1,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-08-21 11:18:37'),(27,10,'0,1,10','電腦資訊部',NULL,14,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(28,10,'0,1,10','毛料采購部',NULL,15,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(29,10,'0,1,10','產品發展部',NULL,16,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(30,10,'0,1,10','輔料采購部',NULL,17,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(31,10,'0,1,10','海外部',NULL,18,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(32,10,'0,1,10','財務部',NULL,19,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(33,10,'0,1,10','審計部',NULL,20,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(34,10,'0,1,10','外事公關部',NULL,21,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(35,10,'0,1,10','營業部',NULL,22,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(36,10,'0,1,10','可持續發展部',NULL,23,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(37,10,'0,1,10','廠務采購部',NULL,24,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(38,10,'0,1,10','品管部(QA)',NULL,25,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(39,10,'0,1,10','行政和人力資源部',NULL,26,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(40,10,'0,1,10','辦部',NULL,27,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(41,10,'0,1,10','發織部',NULL,28,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(42,10,'0,1,10','電機生產部',NULL,29,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(43,10,'0,1,10','電機零件部',NULL,30,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(44,10,'0,1,10','圓筒織機部',NULL,31,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(45,10,'0,1,10','前整生產部',NULL,32,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(46,10,'0,1,10','后整生產部',NULL,33,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(47,10,'0,1,10','物流部',NULL,6,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(54,10,'0,1,10','后勤服務中心',NULL,34,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(55,10,'0,1,10','工程部',NULL,35,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(56,10,'0,1,10','環境管理部',NULL,36,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(60,10,'0,1,10','董事局',NULL,37,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(63,10,'0,1,10','中華區管理',NULL,38,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(64,10,'0,1,10','集團行政部',NULL,39,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(65,10,'0,1,10','董事辦公室',NULL,40,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(66,10,'0,1,10','會計部',NULL,41,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(67,10,'0,1,10','押匯部',NULL,42,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(68,10,'0,1,10','物業發展部',NULL,43,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(69,10,'0,1,10','投資部',NULL,44,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(71,10,'0,1,10','電腦織機部-2A',NULL,1,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-07-25 10:25:08'),(72,10,'0,1,10','電腦織機部-3A       ',NULL,2,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-07-25 10:25:17'),(73,10,'0,1,10','電腦織機部-3B       ',NULL,3,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-07-25 10:25:25'),(74,10,'0,1,10','電腦織機部-4A       ',NULL,4,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-07-25 10:25:38'),(75,10,'0,1,10','電腦織機部-4B       ',NULL,5,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-24 10:28:53','admin','2026-07-25 10:25:48'),(76,10,'0,1,10','電腦織機部-5A',NULL,45,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(77,10,'0,1,10','電腦織機部-5B',NULL,46,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(78,10,'0,1,10','電腦織機部-6B',NULL,47,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(79,10,'0,1,10','電腦織機部-7B',NULL,48,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(81,10,'0,1,10','后整工場-2F',NULL,49,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(82,10,'0,1,10','后整工場-3F',NULL,50,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(83,10,'0,1,10','后整工場-4F',NULL,51,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(84,10,'0,1,10','后整工場-5F',NULL,52,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(85,10,'0,1,10','前整工場',NULL,53,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(87,10,'0,1,10','總務部',NULL,56,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(88,10,'0,1,10','本銷部',NULL,54,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(89,10,'0,1,10','MCO',NULL,55,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(91,10,'0,1,10','電機維護部',NULL,57,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(93,10,'0,1,10','行政部',NULL,58,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(95,10,'0,1,10','對色部',NULL,59,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(96,10,'0,1,10','扎染生產部',NULL,60,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(99,10,'0,1,10','QC部',NULL,61,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(101,10,'0,1,10','工業工程部',NULL,7,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(102,10,'0,1,10','鞋部',NULL,8,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(103,10,'0,1,10','染廠管理','Dyeing Plant Management',9,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(104,10,'0,1,10','研發工場',NULL,10,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(105,10,'0,1,10','總經辦',NULL,11,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(106,10,'0,1,10','排缸部',NULL,12,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL),(107,10,'0,1,10','飛織生產部',NULL,13,'若依','15888888888','ry@qq.com','0','0','admin','2026-07-28 15:15:55','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典編碼',
  `dict_sort` int(11) DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典標籤',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典鍵值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典類型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '樣式屬性（其他樣式擴展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回顯樣式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默認（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '狀態（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='字典數據表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2026-07-24 10:28:55','',NULL,'性別男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2026-07-24 10:28:55','',NULL,'性別女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2026-07-24 10:28:55','',NULL,'性別未知'),(4,1,'顯示','0','sys_show_hide','','primary','Y','0','admin','2026-07-24 10:28:55','',NULL,'顯示選單'),(5,2,'隱藏','1','sys_show_hide','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'隱藏選單'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2026-07-24 10:28:55','',NULL,'正常狀態'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'停用狀態'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2026-07-24 10:28:55','',NULL,'正常狀態'),(9,2,'暫停','1','sys_job_status','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'停用狀態'),(10,1,'默認','DEFAULT','sys_job_group','','','Y','0','admin','2026-07-24 10:28:55','',NULL,'默認分組'),(11,2,'系統','SYSTEM','sys_job_group','','','N','0','admin','2026-07-24 10:28:55','',NULL,'系統分組'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2026-07-24 10:28:55','',NULL,'系統默認是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'系統默認否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2026-07-24 10:28:55','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2026-07-24 10:28:55','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2026-07-24 10:28:55','',NULL,'正常狀態'),(17,2,'關閉','1','sys_notice_status','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'關閉狀態'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2026-07-24 10:28:55','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2026-07-24 10:28:55','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2026-07-24 10:28:55','',NULL,'修改操作'),(21,3,'刪除','3','sys_oper_type','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'刪除操作'),(22,4,'授權','4','sys_oper_type','','primary','N','0','admin','2026-07-24 10:28:55','',NULL,'授權操作'),(23,5,'導出','5','sys_oper_type','','warning','N','0','admin','2026-07-24 10:28:55','',NULL,'導出操作'),(24,6,'導入','6','sys_oper_type','','warning','N','0','admin','2026-07-24 10:28:55','',NULL,'導入操作'),(25,7,'強退','7','sys_oper_type','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'強退操作'),(26,8,'生成代碼','8','sys_oper_type','','warning','N','0','admin','2026-07-24 10:28:55','',NULL,'生成操作'),(27,9,'清空數據','9','sys_oper_type','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2026-07-24 10:28:55','',NULL,'正常狀態'),(29,2,'失敗','1','sys_common_status','','danger','N','0','admin','2026-07-24 10:28:55','',NULL,'停用狀態');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主鍵',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名稱',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典類型',
  `status` char(1) DEFAULT '0' COMMENT '狀態（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='字典類型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用戶性別','sys_user_sex','0','admin','2026-07-24 10:28:55','',NULL,'用戶性別列表'),(2,'選單狀態','sys_show_hide','0','admin','2026-07-24 10:28:55','',NULL,'選單狀態列表'),(3,'系統開關','sys_normal_disable','0','admin','2026-07-24 10:28:55','',NULL,'系統開關列表'),(4,'任務狀態','sys_job_status','0','admin','2026-07-24 10:28:55','',NULL,'任務狀態列表'),(5,'任務分組','sys_job_group','0','admin','2026-07-24 10:28:55','',NULL,'任務分組列表'),(6,'系統是否','sys_yes_no','0','admin','2026-07-24 10:28:55','',NULL,'系統是否列表'),(7,'通知類型','sys_notice_type','0','admin','2026-07-24 10:28:55','',NULL,'通知類型列表'),(8,'通知狀態','sys_notice_status','0','admin','2026-07-24 10:28:55','',NULL,'通知狀態列表'),(9,'操作類型','sys_oper_type','0','admin','2026-07-24 10:28:55','',NULL,'操作類型列表'),(10,'系統狀態','sys_common_status','0','admin','2026-07-24 10:28:55','',NULL,'登錄狀態列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任務ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任務名稱',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任務組名',
  `invoke_target` varchar(500) NOT NULL COMMENT '調用目標字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron執行表達式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '計劃執行錯誤策略（1立即執行 2執行一次 3放棄執行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否並發執行（0允許 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '狀態（0正常 1暫停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT '' COMMENT '備註信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='定時任務調度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系統默認（無參）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2026-07-24 10:28:55','',NULL,''),(2,'系統默認（有參）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2026-07-24 10:28:55','',NULL,''),(3,'系統默認（多參）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2026-07-24 10:28:55','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任務日誌ID',
  `job_name` varchar(64) NOT NULL COMMENT '任務名稱',
  `job_group` varchar(64) NOT NULL COMMENT '任務組名',
  `invoke_target` varchar(500) NOT NULL COMMENT '調用目標字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日誌信息',
  `status` char(1) DEFAULT '0' COMMENT '執行狀態（0正常 1失敗）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '異常信息',
  `start_time` datetime DEFAULT NULL COMMENT '執行開始時間',
  `end_time` datetime DEFAULT NULL COMMENT '執行結束時間',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='定時任務調度日誌表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '訪問ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用戶賬號',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登錄IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登錄地點',
  `browser` varchar(50) DEFAULT '' COMMENT '瀏覽器類型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系統',
  `status` char(1) DEFAULT '0' COMMENT '登錄狀態（0成功 1失敗）',
  `msg` varchar(1000) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '訪問時間',
  PRIMARY KEY (`info_id`) USING BTREE,
  KEY `idx_sys_logininfor_s` (`status`) USING BTREE,
  KEY `idx_sys_logininfor_lt` (`login_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系統訪問記錄';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (1,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:29:21'),(2,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:29:33'),(3,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:29:41'),(4,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:29:49'),(5,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:30:01'),(6,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:30:08'),(7,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:30:24'),(8,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:34:00'),(9,'admin','127.0.0.1','内网IP','WindowsPowerShell 5.1.26100.9444','Windows 10.0','0','登录成功','2026-09-28 14:35:40');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '選單ID',
  `menu_name` varchar(50) NOT NULL COMMENT '選單名稱',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父選單ID',
  `order_num` int(11) DEFAULT 0 COMMENT '顯示順序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '組件路徑',
  `query` varchar(255) DEFAULT NULL COMMENT '路由參數',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名稱',
  `is_frame` int(11) DEFAULT 1 COMMENT '是否為外鏈（0是 1否）',
  `is_cache` int(11) DEFAULT 0 COMMENT '是否緩存（0緩存 1不緩存）',
  `menu_type` char(1) DEFAULT '' COMMENT '選單類型（M目錄 C選單 F按鈕）',
  `visible` char(1) DEFAULT '0' COMMENT '選單狀態（0顯示 1隱藏）',
  `status` char(1) DEFAULT '0' COMMENT '選單狀態（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '權限標識',
  `icon` varchar(100) DEFAULT '#' COMMENT '選單圖標',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT '' COMMENT '備註',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='選單權限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系統管理',0,4,'system',NULL,'','',1,0,'M','0','0','','system','admin','2026-07-24 10:28:54','admin','2026-08-11 08:55:08','系統管理目錄'),(2,'系統監控',0,6,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2026-07-24 10:28:54','admin','2026-08-11 08:55:32','系統監控目錄'),(3,'系統工具',0,5,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2026-07-24 10:28:54','admin','2026-08-11 08:55:19','系統工具目錄'),(100,'用戶管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2026-07-24 10:28:54','',NULL,'用戶管理選單'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2026-07-24 10:28:54','',NULL,'角色管理選單'),(102,'選單管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2026-07-24 10:28:54','',NULL,'選單管理選單'),(103,'部門管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2026-07-24 10:28:54','',NULL,'部門管理選單'),(104,'崗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2026-07-24 10:28:54','',NULL,'崗位管理選單'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2026-07-24 10:28:54','',NULL,'字典管理選單'),(106,'參數設置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2026-07-24 10:28:54','',NULL,'參數設置選單'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2026-07-24 10:28:54','',NULL,'通知公告選單'),(108,'日誌管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2026-07-24 10:28:54','',NULL,'日誌管理選單'),(109,'在線用戶',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2026-07-24 10:28:54','',NULL,'在線用戶選單'),(110,'定時任務',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2026-07-24 10:28:54','',NULL,'定時任務選單'),(111,'數據監控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2026-07-24 10:28:54','',NULL,'數據監控選單'),(112,'服務監控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2026-07-24 10:28:54','',NULL,'服務監控選單'),(113,'緩存監控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2026-07-24 10:28:54','',NULL,'緩存監控選單'),(114,'緩存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2026-07-24 10:28:54','',NULL,'緩存列表選單'),(115,'表單構建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2026-07-24 10:28:54','',NULL,'表單構建選單'),(116,'代碼生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2026-07-24 10:28:54','',NULL,'代碼生成選單'),(117,'系統接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2026-07-24 10:28:54','',NULL,'系統接口選單'),(500,'操作日誌',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2026-07-24 10:28:54','',NULL,'操作日誌選單'),(501,'登錄日誌',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2026-07-24 10:28:54','',NULL,'登錄日誌選單'),(1000,'用戶查詢',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1001,'用戶新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1002,'用戶修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1003,'用戶刪除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1004,'用戶導出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1005,'用戶導入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2026-07-24 10:28:54','',NULL,''),(1006,'重置密碼',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2026-07-24 10:28:54','',NULL,''),(1007,'角色查詢',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1010,'角色刪除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1011,'角色導出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1012,'選單查詢',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1013,'選單新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1014,'選單修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1015,'選單刪除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1016,'部門查詢',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1017,'部門新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1018,'部門修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1019,'部門刪除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1020,'崗位查詢',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1021,'崗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1022,'崗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1023,'崗位刪除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1024,'崗位導出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1025,'字典查詢',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1028,'字典刪除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1029,'字典導出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1030,'參數查詢',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1031,'參數新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1032,'參數修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1033,'參數刪除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1034,'參數導出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1035,'公告查詢',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1038,'公告刪除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1039,'操作查詢',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1040,'操作刪除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1041,'日誌導出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1042,'登錄查詢',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1043,'登錄刪除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1044,'日誌導出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1045,'賬戶解鎖',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2026-07-24 10:28:54','',NULL,''),(1046,'在線查詢',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1047,'批量強退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2026-07-24 10:28:54','',NULL,''),(1048,'單條強退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2026-07-24 10:28:54','',NULL,''),(1049,'任務查詢',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1050,'任務新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2026-07-24 10:28:54','',NULL,''),(1051,'任務修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1052,'任務刪除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1053,'狀態修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2026-07-24 10:28:54','',NULL,''),(1054,'任務導出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2026-07-24 10:28:54','',NULL,''),(1055,'生成查詢',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2026-07-24 10:28:54','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2026-07-24 10:28:54','',NULL,''),(1057,'生成刪除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2026-07-24 10:28:54','',NULL,''),(1058,'導入代碼',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2026-07-24 10:28:54','',NULL,''),(1059,'預覽代碼',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2026-07-24 10:28:54','',NULL,''),(1060,'生成代碼',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2026-07-24 10:28:54','',NULL,''),(2001,'會議',0,0,'mtg',NULL,NULL,'',1,0,'M','0','0','','build','admin','2026-07-27 10:00:28','admin','2026-07-27 10:07:01',''),(2002,'會議室管理',2001,0,'mtg/room/index','mtg/room/index',NULL,'',0,0,'C','0','0','mtg:room:list','redis-list','admin','2026-07-27 10:02:17','admin','2026-07-27 10:11:21',''),(2003,'設備管理',2001,1,'mtg/device/index','mtg/device/index',NULL,'',0,0,'C','0','0','mtg:device:list','phone','admin','2026-07-27 10:03:21','admin','2026-07-27 10:11:41',''),(2004,'預約申請',2001,2,'calendar','mtg/calendar/index',NULL,'',0,0,'C','0','0','mtg:booking:calendar','date','admin','2026-07-27 15:22:37','',NULL,'会议室预约日历视图'),(2005,'預約管理',2001,3,'booking','mtg/booking/index',NULL,'',0,0,'C','0','0','mtg:booking:list','list','admin','2026-07-27 16:21:56','',NULL,'管理端预约列表'),(2006,'固定預約',2001,4,'fixedBooking','mtg/fixedBooking/index',NULL,'',0,0,'C','0','0','mtg:fixedBooking:list','bell','admin','2026-07-28 09:54:59','',NULL,'固定预约管理'),(2007,'预约查询',2004,1,'#','',NULL,'',1,0,'F','0','0','mtg:booking:query','#','admin','2026-07-28 10:46:56','',NULL,''),(2008,'预约新增',2004,2,'#','',NULL,'',1,0,'F','0','0','mtg:booking:add','#','admin','2026-07-28 10:46:56','',NULL,''),(2009,'预约修改',2004,3,'#','',NULL,'',1,0,'F','0','0','mtg:booking:edit','#','admin','2026-07-28 10:46:56','',NULL,''),(2010,'预约删除',2004,4,'#','',NULL,'',1,0,'F','0','0','mtg:booking:remove','#','admin','2026-07-28 10:46:56','',NULL,''),(2011,'预约取消',2004,5,'#','',NULL,'',1,0,'F','0','0','mtg:booking:cancel','#','admin','2026-07-28 10:46:56','',NULL,''),(2012,'预约日历',2004,6,'#','',NULL,'',1,0,'F','0','0','mtg:booking:calendar','#','admin','2026-07-28 10:46:56','',NULL,''),(2013,'预约管理查询',2005,1,'#','',NULL,'',1,0,'F','0','0','mtg:booking:list','#','admin','2026-07-28 10:46:56','',NULL,''),(2014,'固定预约查询',2006,1,'#','',NULL,'',1,0,'F','0','0','mtg:fixedBooking:query','#','admin','2026-07-28 10:46:56','',NULL,''),(2015,'固定预约新增',2006,2,'#','',NULL,'',1,0,'F','0','0','mtg:fixedBooking:add','#','admin','2026-07-28 10:46:56','',NULL,''),(2016,'固定预约修改',2006,3,'#','',NULL,'',1,0,'F','0','0','mtg:fixedBooking:edit','#','admin','2026-07-28 10:46:56','',NULL,''),(2017,'固定预约删除',2006,4,'#','',NULL,'',1,0,'F','0','0','mtg:fixedBooking:remove','#','admin','2026-07-28 10:46:56','',NULL,''),(2018,'会议室新增',2002,0,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:add','#','admin','2026-07-28 11:00:42','',NULL,''),(2019,'会议室修改',2002,1,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:edit','#','admin','2026-07-28 11:01:09','admin','2026-07-28 11:01:29',''),(2020,'会议室删除',2002,2,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:remove','#','admin','2026-07-28 11:01:49','',NULL,''),(2021,'会议室查询',2002,3,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:query','#','admin','2026-07-28 11:02:26','',NULL,''),(2022,'会议室导出',2002,4,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:export','#','admin','2026-07-28 11:03:01','',NULL,''),(2023,'会议室状态修改',2002,5,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:status','#','admin','2026-07-28 11:03:21','',NULL,''),(2024,'会议室维护操作',2002,6,'',NULL,NULL,'',1,0,'F','0','0','mtg:room:maintain','#','admin','2026-07-28 11:03:43','',NULL,''),(2025,'设备新增',2003,0,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:add','#','admin','2026-07-28 11:04:22','',NULL,''),(2026,'设备修改',2003,1,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:edit','#','admin','2026-07-28 11:04:42','',NULL,''),(2027,'设备删除',2003,2,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:remove','#','admin','2026-07-28 11:05:06','',NULL,''),(2028,'设备查询',2003,3,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:query','#','admin','2026-07-28 11:05:30','',NULL,''),(2029,'设备导出',2003,4,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:export','#','admin','2026-07-28 11:05:52','',NULL,''),(2030,'设备状态修改',2003,5,'',NULL,NULL,'',1,0,'F','0','0','mtg:device:status','#','admin','2026-07-28 11:06:11','',NULL,''),(2032,'服務管理',2001,7,'service','mtg/service/index',NULL,'',1,0,'C','0','0','mtg:service:list','peoples','admin','2026-08-03 10:27:36','',NULL,NULL),(2033,'服务查询',2032,1,'#','',NULL,'',1,0,'F','0','0','mtg:service:query','#','admin','2026-08-03 10:27:36','',NULL,NULL),(2034,'服务新增',2032,2,'#','',NULL,'',1,0,'F','0','0','mtg:service:add','#','admin','2026-08-03 10:27:36','',NULL,NULL),(2035,'服务修改',2032,3,'#','',NULL,'',1,0,'F','0','0','mtg:service:edit','#','admin','2026-08-03 10:27:36','',NULL,NULL),(2036,'服务删除',2032,4,'#','',NULL,'',1,0,'F','0','0','mtg:service:remove','#','admin','2026-08-03 10:27:36','',NULL,NULL),(2037,'服务导出',2032,5,'#','',NULL,'',1,0,'F','0','0','mtg:service:export','#','admin','2026-08-03 10:27:36','',NULL,NULL),(2044,'報表',0,3,'form',NULL,NULL,'',1,0,'M','0','0','','form','admin','2026-08-06 11:52:09','admin','2026-08-11 08:54:59',''),(2045,'中國營業接單數',2044,0,'chinaOrder','mtg/report/chinaOrder',NULL,'',1,0,'C','0','0','','more-up','admin','2026-08-06 13:39:40','admin','2026-08-06 13:43:06',''),(2046,'用車',0,1,'car',NULL,NULL,'',1,0,'M','0','0','','international','admin','2026-08-11 08:53:12','admin','2026-08-11 08:54:18',''),(2047,'車輛信息',2046,0,'car/vehicle/index','car/vehicle/index',NULL,'',1,0,'C','0','0',NULL,'component','admin','2026-08-11 08:57:51','',NULL,''),(2048,'查询车辆信息',2047,0,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:list','#','admin','2026-08-11 09:25:24','',NULL,''),(2049,'查询车辆详情',2047,1,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:query','#','admin','2026-08-11 09:25:57','',NULL,''),(2050,'新增车辆',2047,2,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:add','#','admin','2026-08-11 09:26:40','',NULL,''),(2051,'修改车辆信息',2047,3,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:edit','#','admin','2026-08-11 09:27:21','',NULL,''),(2052,'删除车辆',2047,4,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:remove','#','admin','2026-08-11 09:27:39','',NULL,''),(2053,'导出车辆信息',2047,5,'',NULL,NULL,'',1,0,'F','0','0','car:vehicle:export','#','admin','2026-08-11 09:28:01','',NULL,''),(2100,'司機信息',2046,1,'car/driver/index','car/driver/index',NULL,'',1,0,'C','0','0','','user','admin','2026-08-11 09:30:53','admin','2026-08-11 09:58:36','司機信息菜单'),(2101,'查询司机信息',2100,0,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:list','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2102,'查询司机详情',2100,1,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:query','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2103,'新增司机',2100,2,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:add','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2104,'修改司机信息',2100,3,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:edit','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2105,'删除司机',2100,4,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:remove','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2106,'导出司机信息',2100,5,NULL,NULL,NULL,'',1,0,'F','0','0','car:driver:export','#','admin','2026-08-11 09:30:53','admin','2026-08-11 09:30:53',NULL),(2111,'用車申請',2046,2,'car/apply/index','car/apply/index',NULL,'',1,0,'C','0','0',NULL,'form','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07','用車申請菜单'),(2112,'查询用车申请',2111,0,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:list','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2113,'查询用车申请详情',2111,1,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:query','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2114,'新增用车申请',2111,2,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:add','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2115,'修改用车申请',2111,3,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:edit','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2116,'删除用车申请',2111,4,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:remove','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2117,'导出用车申请',2111,5,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:export','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2118,'审批用车申请',2111,6,NULL,NULL,NULL,'',1,0,'F','0','0','car:apply:audit','#','admin','2026-08-11 12:11:07','admin','2026-08-11 12:11:07',NULL),(2127,'车辆报表',2046,3,'expense','car/expense/index',NULL,'',1,0,'C','0','0','car:expense:list','tab','admin','2026-08-12 15:47:28','admin','2026-08-18 12:03:05','车辆费用记录菜单'),(2128,'查询车辆费用',2127,1,'','',NULL,'',1,0,'F','0','0','car:expense:list','#','admin','2026-08-12 15:47:28','',NULL,''),(2129,'查询车辆费用详情',2127,2,'','',NULL,'',1,0,'F','0','0','car:expense:query','#','admin','2026-08-12 15:47:28','',NULL,''),(2130,'新增车辆费用',2127,3,'','',NULL,'',1,0,'F','0','0','car:expense:add','#','admin','2026-08-12 15:47:28','',NULL,''),(2131,'修改车辆费用',2127,4,'','',NULL,'',1,0,'F','0','0','car:expense:edit','#','admin','2026-08-12 15:47:28','',NULL,''),(2132,'删除车辆费用',2127,5,'','',NULL,'',1,0,'F','0','0','car:expense:remove','#','admin','2026-08-12 15:47:28','',NULL,''),(2133,'导出车辆费用',2127,6,'','',NULL,'',1,0,'F','0','0','car:expense:export','#','admin','2026-08-12 15:47:28','',NULL,''),(2134,'出入记录查询',2139,1,'#','',NULL,'',1,0,'F','0','0','car:accessRecord:query','#','admin','2026-08-14 14:52:07','',NULL,''),(2135,'出入记录新增',2139,2,'#','',NULL,'',1,0,'F','0','0','car:accessRecord:add','#','admin','2026-08-14 14:52:07','',NULL,''),(2136,'出入记录修改',2139,3,'#','',NULL,'',1,0,'F','0','0','car:accessRecord:edit','#','admin','2026-08-14 14:52:07','',NULL,''),(2137,'出入记录删除',2139,4,'#','',NULL,'',1,0,'F','0','0','car:accessRecord:remove','#','admin','2026-08-14 14:52:07','',NULL,''),(2138,'出入记录导出',2139,5,'#','',NULL,'',1,0,'F','0','0','car:accessRecord:export','#','admin','2026-08-14 14:52:07','',NULL,''),(2139,'出入记录',2046,5,'accessRecord','car/accessRecord/index',NULL,'',1,0,'C','0','0','car:accessRecord:list','guide','admin','2026-08-14 14:53:18','admin','2026-08-22 12:02:13','车辆出入记录菜单'),(2140,'查询',2045,0,'',NULL,NULL,'',1,0,'F','0','0','mtg:report:list','#','admin','2026-08-18 11:02:31','',NULL,''),(2141,'导出',2045,1,'',NULL,NULL,'',1,0,'F','0','0','mtg:report:export','#','admin','2026-08-18 11:02:55','',NULL,''),(2142,'门卫出入登记',2046,4,'duty','car/duty/index',NULL,'',1,0,'C','0','0','car:duty:view','log','admin','2026-08-22 11:16:54','admin','2026-08-22 12:02:01','保安值班页面'),(2143,'值班操作',2142,1,'#','',NULL,'',1,0,'F','0','0','car:duty:operate','#','admin','2026-08-22 11:16:54','',NULL,'出厂/回场操作权限'),(2146,'补签',2111,8,'','',NULL,'',1,0,'F','0','0','car:apply:supplement','#','admin','2026-08-26 10:15:04','',NULL,'补签权限');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告標題',
  `notice_type` char(1) NOT NULL COMMENT '公告類型（1通知 2公告）',
  `notice_content` longblob DEFAULT NULL COMMENT '公告內容',
  `status` char(1) DEFAULT '0' COMMENT '公告狀態（0正常 1關閉）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(255) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (11,'测试','2','<p>测试</p>','0','admin','2026-08-19 10:49:53','',NULL,NULL),(12,'测试','1','<p>测试</p>','0','admin','2026-08-19 10:50:11','',NULL,NULL);
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice_msg`
--

DROP TABLE IF EXISTS `sys_notice_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notice_msg` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `msg_title` varchar(255) DEFAULT NULL COMMENT '消息标题',
  `msg_content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `msg_type` char(1) DEFAULT '1' COMMENT '消息类型 1=通知 2=公告',
  `notice_type` char(1) DEFAULT '1' COMMENT '通知类型 1=用车申请',
  `business_id` bigint(20) DEFAULT NULL COMMENT '关联业务ID',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `receiver_type` char(1) DEFAULT '1' COMMENT '接收对象类型 1=指定用户 2=角色',
  `receiver` varchar(255) DEFAULT NULL COMMENT '接收对象（用户ID列表或角色）',
  `is_read` char(1) DEFAULT '0' COMMENT '已读状态 0=未读 1=已读',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统消息通知';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice_msg`
--

LOCK TABLES `sys_notice_msg` WRITE;
/*!40000 ALTER TABLE `sys_notice_msg` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice_msg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice_read`
--

DROP TABLE IF EXISTS `sys_notice_read`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notice_read` (
  `read_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '已讀主鍵',
  `notice_id` int(11) NOT NULL COMMENT '公告id',
  `user_id` bigint(20) NOT NULL COMMENT '用戶id',
  `read_time` datetime NOT NULL COMMENT '閱讀時間',
  PRIMARY KEY (`read_id`) USING BTREE,
  UNIQUE KEY `uk_user_notice` (`user_id`,`notice_id`) USING BTREE COMMENT '同一用戶同一公告只記錄一次'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='公告已讀記錄表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice_read`
--

LOCK TABLES `sys_notice_read` WRITE;
/*!40000 ALTER TABLE `sys_notice_read` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice_read` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日誌主鍵',
  `title` varchar(50) DEFAULT '' COMMENT '模組標題',
  `business_type` int(11) DEFAULT 0 COMMENT '業務類型（0其它 1新增 2修改 3刪除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名稱',
  `request_method` varchar(10) DEFAULT '' COMMENT '請求方式',
  `operator_type` int(11) DEFAULT 0 COMMENT '操作類別（0其它 1後臺用戶 2手機端用戶）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人員',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部門名稱',
  `oper_url` varchar(255) DEFAULT '' COMMENT '請求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主機地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地點',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '請求參數',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回參數',
  `status` int(11) DEFAULT 0 COMMENT '操作狀態（0正常 1異常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '錯誤消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作時間',
  `cost_time` bigint(20) DEFAULT 0 COMMENT '消耗時間',
  PRIMARY KEY (`oper_id`) USING BTREE,
  KEY `idx_sys_oper_log_bt` (`business_type`) USING BTREE,
  KEY `idx_sys_oper_log_s` (`status`) USING BTREE,
  KEY `idx_sys_oper_log_ot` (`oper_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='操作日誌記錄';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '崗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '崗位編碼',
  `post_name` varchar(50) NOT NULL COMMENT '崗位名稱',
  `post_sort` int(11) NOT NULL COMMENT '顯示順序',
  `status` char(1) NOT NULL COMMENT '狀態（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='崗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事長',1,'0','admin','2026-07-24 10:28:54','',NULL,''),(2,'se','項目經理',2,'0','admin','2026-07-24 10:28:54','',NULL,''),(3,'hr','人力資源',3,'0','admin','2026-07-24 10:28:54','',NULL,''),(4,'user','普通員工',4,'0','admin','2026-07-24 10:28:54','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名稱',
  `role_key` varchar(100) NOT NULL COMMENT '角色權限字符串',
  `role_sort` int(11) NOT NULL COMMENT '顯示順序',
  `data_scope` char(1) DEFAULT '1' COMMENT '數據範圍（1：全部數據權限 2：自定數據權限 3：本部門數據權限 4：本部門及以下數據權限）',
  `menu_check_strictly` tinyint(1) DEFAULT 1 COMMENT '選單樹選擇項是否關聯顯示',
  `dept_check_strictly` tinyint(1) DEFAULT 1 COMMENT '部門樹選擇項是否關聯顯示',
  `status` char(1) NOT NULL COMMENT '角色狀態（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '刪除標誌（0代表存在 2代表刪除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超級管理員','admin',1,'1',1,1,'0','0','admin','2026-07-24 10:28:54','',NULL,'超級管理員'),(2,'普通角色','common',2,'2',0,1,'0','0','admin','2026-07-24 10:28:54','admin','2026-08-26 11:35:33','普通角色'),(100,'管理员','adminUser',0,'1',1,1,'0','0','admin','2026-08-18 10:39:25','admin','2026-08-26 10:23:30',NULL),(101,'工人','user_all',0,'1',1,1,'0','2','admin','2026-08-21 11:38:45','',NULL,NULL),(102,'保安','doorkeeper',3,'1',1,1,'0','0','admin','2026-08-25 09:58:52','admin','2026-08-26 10:24:00',NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部門ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色和部門關聯表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '選單ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色和選單關聯表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,2001),(2,2004),(2,2005),(2,2006),(2,2007),(2,2008),(2,2011),(2,2012),(2,2014),(2,2015),(2,2021),(2,2025),(2,2026),(2,2027),(2,2028),(2,2029),(2,2030),(2,2033),(2,2034),(2,2035),(2,2036),(2,2037),(2,2046),(2,2111),(2,2112),(2,2113),(2,2114),(2,2115),(2,2117),(2,2128),(2,2129),(2,2130),(2,2131),(2,2132),(100,2001),(100,2002),(100,2003),(100,2004),(100,2005),(100,2006),(100,2007),(100,2008),(100,2009),(100,2010),(100,2011),(100,2012),(100,2013),(100,2014),(100,2015),(100,2016),(100,2017),(100,2018),(100,2019),(100,2020),(100,2021),(100,2022),(100,2023),(100,2024),(100,2025),(100,2026),(100,2027),(100,2028),(100,2029),(100,2030),(100,2032),(100,2033),(100,2034),(100,2035),(100,2036),(100,2037),(100,2044),(100,2045),(100,2046),(100,2047),(100,2048),(100,2049),(100,2050),(100,2051),(100,2052),(100,2053),(100,2100),(100,2101),(100,2102),(100,2103),(100,2104),(100,2105),(100,2106),(100,2111),(100,2112),(100,2113),(100,2114),(100,2115),(100,2116),(100,2117),(100,2118),(100,2127),(100,2128),(100,2129),(100,2130),(100,2131),(100,2132),(100,2133),(100,2134),(100,2135),(100,2136),(100,2137),(100,2138),(100,2139),(100,2140),(100,2141),(100,2146),(102,2046),(102,2047),(102,2048),(102,2134),(102,2135),(102,2138),(102,2139),(102,2142),(102,2143);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用戶ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部門ID',
  `user_name` varchar(30) NOT NULL COMMENT '用戶賬號',
  `nick_name` varchar(30) NOT NULL COMMENT '用戶暱稱',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用戶類型（00系統用戶）',
  `email` varchar(100) DEFAULT '' COMMENT '用戶郵箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手機號碼',
  `sex` char(1) DEFAULT '0' COMMENT '用戶性別（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '頭像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密碼',
  `status` char(1) DEFAULT '0' COMMENT '賬號狀態（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '刪除標誌（0代表存在 2代表刪除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最後登錄IP',
  `login_date` datetime DEFAULT NULL COMMENT '最後登錄時間',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密碼最後更新時間',
  `create_by` varchar(64) DEFAULT '' COMMENT '創建者',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1579 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用戶信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','超级管理员','00','ry@163.com','15888888888','0','/profile/avatar/2026/08/20/77b6187137a4499f9d3becced55ef434.jpg','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2026-09-28 14:35:40','2026-07-24 10:28:54','admin','2026-07-24 10:28:54','','2026-08-20 14:32:41','管理員'),(2,105,'ry','普通用户','00','ry@qq.com','15666666666','1','','$2a$10$dbLdwKgjQenhFrYLuBRMYuBYE4zSqWEn91qDgioTyL2wJYGZBHZgO','0','0','127.0.0.1','2026-08-18 11:27:45','2026-08-18 10:46:26','admin','2026-07-24 10:28:54','admin','2026-08-18 11:05:17','測試員');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用戶ID',
  `post_id` bigint(20) NOT NULL COMMENT '崗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用戶與崗位關聯表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用戶ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用戶和角色關聯表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,100);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-28 14:35:40
