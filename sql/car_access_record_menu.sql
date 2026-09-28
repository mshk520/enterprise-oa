-- 出入记录菜单（挂在 用车 一级菜单 2046 下）
SET @parentId = 2046;

-- 二级菜单：出入记录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录', @parentId, 4, 'accessRecord', 'car/accessRecord/index', NULL, '', 1, 0, 'C', '0', '0', 'car:accessRecord:list', 'log', 'admin', sysdate(), '车辆出入记录菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '出入记录' AND parent_id = @parentId);

SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '出入记录' AND parent_id = @parentId);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:accessRecord:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:accessRecord:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:accessRecord:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:accessRecord:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:accessRecord:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:accessRecord:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:accessRecord:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:accessRecord:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '出入记录导出', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:accessRecord:export', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:accessRecord:export');