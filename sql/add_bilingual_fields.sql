-- 会议室表添加英文字段
ALTER TABLE `mtg_room` ADD COLUMN `room_name_en` varchar(100) DEFAULT NULL COMMENT '会议室名称(英文)' AFTER `room_name`;
ALTER TABLE `mtg_room` ADD COLUMN `floor_en` varchar(20) DEFAULT NULL COMMENT '楼层(英文)' AFTER `floor`;

-- 服务字典表添加英文字段
ALTER TABLE `mtg_service_dict` ADD COLUMN `service_name_en` varchar(50) DEFAULT NULL COMMENT '服务名称(英文)' AFTER `service_name`;