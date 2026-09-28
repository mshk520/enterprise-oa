-- 车辆出入记录表（出厂/回厂时间手动登记）
CREATE TABLE IF NOT EXISTS `car_access_record` (
  `access_id`     bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `plate_number`  varchar(20) NOT NULL COMMENT '车牌号',
  `driver_name`   varchar(50) DEFAULT NULL COMMENT '司机姓名',
  `out_time`      datetime DEFAULT NULL COMMENT '出厂时间',
  `in_time`       datetime DEFAULT NULL COMMENT '回厂时间',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '登记人',
  `del_flag`      char(1) DEFAULT '0' COMMENT '删除标记 0存在 2删除',
  `create_by`     varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`   datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`     varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`   datetime DEFAULT NULL COMMENT '更新时间',
  `remark`        varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`access_id`),
  KEY `idx_plate_number` (`plate_number`),
  KEY `idx_out_time` (`out_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='车辆出入记录表';
