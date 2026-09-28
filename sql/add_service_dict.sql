-- 创建服务管理数据字典表
CREATE TABLE IF NOT EXISTS `mtg_service_dict` (
  `service_id` bigint NOT NULL AUTO_INCREMENT COMMENT '服务ID',
  `service_name` varchar(50) NOT NULL COMMENT '服务名称',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议服务字典表';

-- 插入默认服务数据
INSERT INTO `mtg_service_dict` (`service_name`, `sort`, `status`, `create_by`, `create_time`, `remark`) VALUES
('茶水', 1, '0', 'admin', sysdate(), NULL),
('礦泉水', 2, '0', 'admin', sysdate(), NULL),
('其他需求', 3, '0', 'admin', sysdate(), NULL);

-- 插入服务管理菜单（parent_id=2001 會議）
-- menu_id 使用 2007（在 2006 固定預約 之后）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('服務管理', 2001, 7, 'service', 'mtg/service/index', NULL, '', 1, 0, 'C', '0', '0', 'mtg:service:list', 'peoples', 'admin', sysdate(), '', NULL, NULL);

-- 获取刚插入的菜单ID并插入权限
SET @serviceMenuId = LAST_INSERT_ID();

-- 插入按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('服务查询', @serviceMenuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'mtg:service:query', '#', 'admin', sysdate(), '', NULL, NULL),
('服务新增', @serviceMenuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'mtg:service:add', '#', 'admin', sysdate(), '', NULL, NULL),
('服务修改', @serviceMenuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'mtg:service:edit', '#', 'admin', sysdate(), '', NULL, NULL),
('服务删除', @serviceMenuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'mtg:service:remove', '#', 'admin', sysdate(), '', NULL, NULL),
('服务导出', @serviceMenuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'mtg:service:export', '#', 'admin', sysdate(), '', NULL, NULL);

-- 给管理员角色(role_id=2)授权
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 2, menu_id FROM sys_menu WHERE perms LIKE 'mtg:service:%';