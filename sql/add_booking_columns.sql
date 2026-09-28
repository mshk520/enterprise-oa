ALTER TABLE mtg_booking
    ADD COLUMN booking_type varchar(10) DEFAULT 'hour' COMMENT '预约类型 hour-按小时 day-按天',
    ADD COLUMN booking_date date COMMENT '预约日期',
    ADD COLUMN attendees int DEFAULT 1 COMMENT '参会人数',
    ADD COLUMN contact_phone varchar(20) COMMENT '联系电话',
    ADD COLUMN meeting_link varchar(500) COMMENT '会议链接',
    ADD COLUMN meeting_password varchar(100) COMMENT '会议密码';
