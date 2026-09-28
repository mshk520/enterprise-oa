-- ----------------------------
-- 消息表：用车申请实时推送通知（与 CarApply 联动）
-- 若表不存在则创建；已存在则跳过
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_notice_msg` (
  `msg_id`        bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `msg_title`     varchar(200)  DEFAULT NULL COMMENT '消息标题',
  `msg_content`   varchar(2000) DEFAULT NULL COMMENT '消息内容',
  `msg_type`      char(1)       DEFAULT '1' COMMENT '消息类型：0=系统 1=业务',
  `notice_type`   char(1)       DEFAULT '1' COMMENT '通知类型：1=通知 2=公告',
  `business_id`   bigint(20)    DEFAULT NULL COMMENT '业务ID（如用车申请applyId）',
  `business_type` varchar(64)   DEFAULT NULL COMMENT '业务类型（如 car:apply）',
  `receiver_type` char(1)       DEFAULT '2' COMMENT '接收者类型：1=个人 2=角色/全部',
  `receiver`      varchar(64)   DEFAULT NULL COMMENT '接收者（用户名/角色）',
  `is_read`       char(1)       DEFAULT '0' COMMENT '是否已读：0=未读 1=已读',
  `create_by`     varchar(64)   DEFAULT '' COMMENT '创建者',
  `create_time`   datetime      DEFAULT NULL COMMENT '创建时间',
  `update_by`     varchar(64)   DEFAULT '' COMMENT '更新者',
  `update_time`   datetime      DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`msg_id`),
  KEY `idx_business` (`business_type`, `business_id`),
  KEY `idx_receiver_read` (`receiver`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表（含用车申请实时推送）';
