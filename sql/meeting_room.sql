-- ----------------------------
-- 会议室信息表
-- ----------------------------
DROP TABLE IF EXISTS meeting_room;
CREATE TABLE meeting_room (
  room_id         bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  room_name       varchar(100)    NOT NULL                 COMMENT '会议室名称',
  room_code       varchar(50)     NOT NULL                 COMMENT '会议室编号',
  location        varchar(200)    DEFAULT NULL             COMMENT '位置',
  floor           int(4)          DEFAULT NULL             COMMENT '楼层',
  capacity        int(11)         DEFAULT 0                COMMENT '容纳人数',
  equipment       varchar(500)    DEFAULT NULL             COMMENT '设备配置',
  image           varchar(500)    DEFAULT NULL             COMMENT '会议室图片',
  status          char(1)         DEFAULT '0'              COMMENT '状态（0启用 1停用 2维护中）',
  remark          varchar(500)    DEFAULT ''               COMMENT '备注',
  create_by       varchar(64)     DEFAULT ''               COMMENT '创建者',
  create_time     datetime        DEFAULT NULL             COMMENT '创建时间',
  update_by       varchar(64)     DEFAULT ''               COMMENT '更新者',
  update_time     datetime        DEFAULT NULL             COMMENT '更新时间',
  PRIMARY KEY (room_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='会议室信息表';

INSERT INTO meeting_room VALUES (1,'第一会议室','ROOM-001','C座4F写字楼',4,20,'投影仪,白板,音响',NULL,'0','大型会议室，适合部门会议','admin',NOW(),'',NULL);
INSERT INTO meeting_room VALUES (2,'第二会议室','ROOM-002','C座4F写字楼',4,10,'投影仪,白板',NULL,'0','中型会议室','admin',NOW(),'',NULL);
INSERT INTO meeting_room VALUES (3,'第三会议室','ROOM-003','C座4F写字楼',4,6,'白板',NULL,'0','小型会议室，适合小组讨论','admin',NOW(),'',NULL);
INSERT INTO meeting_room VALUES (4,'贵宾接待室','ROOM-004','B座4F写字楼',4,8,'投影仪,音响,茶歇区',NULL,'0','用于接待重要客户','admin',NOW(),'',NULL);

-- ----------------------------
-- 会议预约表
-- ----------------------------
DROP TABLE IF EXISTS meeting_booking;
CREATE TABLE meeting_booking (
  booking_id      bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  room_id         bigint(20)      NOT NULL                 COMMENT '会议室ID',
  booking_title   varchar(200)    NOT NULL                 COMMENT '会议主题',
  booking_date    date            NOT NULL                 COMMENT '预约日期',
  start_time      time            NOT NULL                 COMMENT '开始时间',
  end_time        time            NOT NULL                 COMMENT '结束时间',
  booking_user    varchar(64)     NOT NULL                 COMMENT '预约人',
  booking_dept    varchar(100)    DEFAULT NULL             COMMENT '预约部门',
  attendees       int(11)         DEFAULT 0                COMMENT '参会人数',
  contact_phone   varchar(20)     DEFAULT NULL             COMMENT '联系电话',
  booking_status  char(1)         DEFAULT '0'              COMMENT '状态（0待审批 1已通过 2已拒绝 3已取消 4已完成）',
  reject_reason   varchar(500)    DEFAULT NULL             COMMENT '拒绝原因',
  is_recurring    char(1)         DEFAULT '0'              COMMENT '是否周期预约（0否 1是）',
  recurring_type  varchar(10)     DEFAULT NULL             COMMENT '周期类型（day/week/month）',
  recurring_end   date            DEFAULT NULL             COMMENT '周期结束日期',
  remark          varchar(500)    DEFAULT ''               COMMENT '备注',
  create_by       varchar(64)     DEFAULT ''               COMMENT '创建者',
  create_time     datetime        DEFAULT NULL             COMMENT '创建时间',
  update_by       varchar(64)     DEFAULT ''               COMMENT '更新者',
  update_time     datetime        DEFAULT NULL             COMMENT '更新时间',
  PRIMARY KEY (booking_id),
  KEY idx_room_date (room_id, booking_date),
  KEY idx_booking_user (booking_user)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='会议预约表';

-- ----------------------------
-- 会议室开放时段表
-- ----------------------------
DROP TABLE IF EXISTS meeting_room_schedule;
CREATE TABLE meeting_room_schedule (
  schedule_id     bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '时段ID',
  room_id         bigint(20)      NOT NULL                 COMMENT '会议室ID',
  day_of_week     tinyint(1)      NOT NULL                 COMMENT '星期几（1周一 2周二 ... 7周日）',
  open_time       time            NOT NULL                 COMMENT '开放开始时间',
  close_time      time            NOT NULL                 COMMENT '开放结束时间',
  is_available    char(1)         DEFAULT '1'              COMMENT '是否可用（0否 1是）',
  create_by       varchar(64)     DEFAULT ''               COMMENT '创建者',
  create_time     datetime        DEFAULT NULL             COMMENT '创建时间',
  update_by       varchar(64)     DEFAULT ''               COMMENT '更新者',
  update_time     datetime        DEFAULT NULL             COMMENT '更新时间',
  PRIMARY KEY (schedule_id),
  KEY idx_room_day (room_id, day_of_week)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='会议室开放时段表';

INSERT INTO meeting_room_schedule (room_id,day_of_week,open_time,close_time,create_by,create_time) SELECT room_id,1,'08:30:00','18:00:00','admin',NOW() FROM meeting_room;
INSERT INTO meeting_room_schedule (room_id,day_of_week,open_time,close_time,create_by,create_time) SELECT room_id,2,'08:30:00','18:00:00','admin',NOW() FROM meeting_room;
INSERT INTO meeting_room_schedule (room_id,day_of_week,open_time,close_time,create_by,create_time) SELECT room_id,3,'08:30:00','18:00:00','admin',NOW() FROM meeting_room;
INSERT INTO meeting_room_schedule (room_id,day_of_week,open_time,close_time,create_by,create_time) SELECT room_id,4,'08:30:00','18:00:00','admin',NOW() FROM meeting_room;
INSERT INTO meeting_room_schedule (room_id,day_of_week,open_time,close_time,create_by,create_time) SELECT room_id,5,'08:30:00','18:00:00','admin',NOW() FROM meeting_room;

-- ----------------------------
-- 预约参会人表
-- ----------------------------
DROP TABLE IF EXISTS meeting_booking_participant;
CREATE TABLE meeting_booking_participant (
  id              bigint(20)      NOT NULL AUTO_INCREMENT COMMENT 'ID',
  booking_id      bigint(20)      NOT NULL                 COMMENT '预约ID',
  user_id         bigint(20)      DEFAULT NULL             COMMENT '用户ID',
  user_name       varchar(64)     DEFAULT NULL             COMMENT '用户名称',
  create_time     datetime        DEFAULT NULL             COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_booking (booking_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='预约参会人表';
