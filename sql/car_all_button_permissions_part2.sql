-- ============================================
-- 用车模块 - 按钮权限补全脚本（第二部分）
-- ============================================

SET @parentMenuId = 2046;

-- ============================================
-- 4. 车辆信息（vehicle）按钮权限
-- ============================================
SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '车辆信息' AND parent_id = @parentMenuId LIMIT 1);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆信息查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:vehicle:query' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆信息新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:vehicle:add' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆信息修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:vehicle:edit' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆信息删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:vehicle:remove' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆信息导出', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:export', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:vehicle:export' AND menu_type = 'F');

-- ============================================
-- 5. 司机信息（driver）按钮权限
-- ============================================
SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '司机信息' AND parent_id = @parentMenuId LIMIT 1);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '司机信息查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:driver:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:driver:query' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '司机信息新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:driver:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:driver:add' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '司机信息修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:driver:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:driver:edit' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '司机信息删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:driver:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:driver:remove' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '司机信息导出', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:driver:export', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:driver:export' AND menu_type = 'F');
