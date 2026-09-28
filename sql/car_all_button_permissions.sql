-- ============================================
-- 用车模块 - 全部按钮权限补全脚本
-- parent_id = 2046（用车管理一级菜单）
-- 使用 WHERE NOT EXISTS 防止重复插入
-- ============================================

SET @parentMenuId = 2046;

-- ============================================
-- 1. 用车申请（apply）按钮权限
-- ============================================
SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '用车申请' AND parent_id = @parentMenuId LIMIT 1);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:query' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:add' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:edit' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:remove' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请审批', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:audit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:audit' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '用车申请导出', @menuId, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:apply:export', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:apply:export' AND menu_type = 'F');

-- ============================================
-- 2. 车辆报表（expense）按钮权限
-- ============================================
SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '车辆报表' AND parent_id = @parentMenuId LIMIT 1);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆报表查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:expense:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:expense:query' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆报表新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:expense:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:expense:add' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆报表修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:expense:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:expense:edit' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆报表删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:expense:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:expense:remove' AND menu_type = 'F');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '车辆报表导出', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:expense:export', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:expense:export' AND menu_type = 'F');

-- ============================================
-- 3. 门卫出入登记（duty）按钮权限
-- ============================================
SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '门卫出入登记' AND parent_id = @parentMenuId LIMIT 1);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '值班操作', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:duty:operate', '#', 'admin', sysdate(), '出厂/回场/紧急出厂操作权限'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:duty:operate' AND menu_type = 'F');
