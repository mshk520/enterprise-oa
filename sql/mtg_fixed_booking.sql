-- 固定预约规则表
CREATE TABLE mtg_fixed_booking (
    fixed_id bigint NOT NULL AUTO_INCREMENT COMMENT '固定预约ID',
    booking_title varchar(50) NOT NULL COMMENT '会议主题',
    start_time varchar(5) NOT NULL COMMENT '开始时间(HH:mm)',
    end_time varchar(5) NOT NULL COMMENT '结束时间(HH:mm)',
    recurrence_type varchar(10) NOT NULL COMMENT '重复类型(daily/weekly/monthly)',
    recurrence_day int DEFAULT NULL COMMENT '重复日(周1-7/月1-31)',
    start_date date NOT NULL COMMENT '生效开始日期',
    end_date date NOT NULL COMMENT '生效结束日期',
    booker_id bigint DEFAULT NULL COMMENT '预约人ID',
    dept_id bigint DEFAULT NULL COMMENT '部门ID',
    attendees int DEFAULT 1 COMMENT '参会人数',
    contact_phone varchar(11) DEFAULT '' COMMENT '联系电话',
    meeting_link varchar(500) DEFAULT '' COMMENT '会议链接',
    meeting_password varchar(100) DEFAULT '' COMMENT '会议密码',
    service_items varchar(500) DEFAULT '' COMMENT '服务项目',
    status char(1) DEFAULT '0' COMMENT '状态(0启用 1停用)',
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT NULL COMMENT '更新时间',
    remark varchar(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (fixed_id)
) ENGINE=InnoDB COMMENT='固定预约规则表';

-- 固定预约-会议室关联表
CREATE TABLE mtg_fixed_room (
    id bigint NOT NULL AUTO_INCREMENT,
    fixed_id bigint NOT NULL COMMENT '固定预约ID',
    room_id bigint NOT NULL COMMENT '会议室ID',
    PRIMARY KEY (id),
    KEY idx_fixed_id (fixed_id),
    KEY idx_room_id (room_id)
) ENGINE=InnoDB COMMENT='固定预约-会议室关联表';

-- mtg_booking 补 fixed_id 字段
ALTER TABLE mtg_booking ADD COLUMN fixed_id bigint DEFAULT NULL COMMENT '固定预约ID' AFTER meeting_password;
ALTER TABLE mtg_booking ADD INDEX idx_fixed_id (fixed_id);
