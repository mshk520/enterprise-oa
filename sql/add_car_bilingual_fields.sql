-- 车辆管理模块添加英文字段（双语支持）

-- 1. 司机表添加英文名字段
ALTER TABLE `car_driver` ADD COLUMN `driver_name_en` varchar(50) DEFAULT NULL COMMENT '司机姓名(英文)' AFTER `driver_name`;

-- 2. 车辆表添加品牌、颜色英文字段
ALTER TABLE `car_vehicle` ADD COLUMN `brand_en` varchar(50) DEFAULT NULL COMMENT '品牌(英文)' AFTER `brand`;
ALTER TABLE `car_vehicle` ADD COLUMN `color_en` varchar(20) DEFAULT NULL COMMENT '颜色(英文)' AFTER `color`;

-- 3. 部门表已有 dept_name_en（若无需自行添加）
-- ALTER TABLE `sys_dept` ADD COLUMN `dept_name_en` varchar(30) DEFAULT NULL COMMENT '部门名称(英文)' AFTER `dept_name`;

-- 4. 车辆出入记录表添加司机英文名、登记人英文名
ALTER TABLE `car_access_record` ADD COLUMN `driver_name_en` varchar(50) DEFAULT NULL COMMENT '司机姓名(英文)' AFTER `driver_name`;
ALTER TABLE `car_access_record` ADD COLUMN `operator_name_en` varchar(50) DEFAULT NULL COMMENT '登记人(英文)' AFTER `operator_name`;

-- 可选：为新增列添加索引（如果需要按英文名查询）
-- CREATE INDEX idx_car_driver_name_en ON car_driver(driver_name_en);
-- CREATE INDEX idx_car_access_driver_name_en ON car_access_record(driver_name_en);
-- CREATE INDEX idx_car_access_operator_name_en ON car_access_record(operator_name_en);