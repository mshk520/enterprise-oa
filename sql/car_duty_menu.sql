-- 保安值班菜单（挂在 用车管理 一级菜单 2046 下）
SET @parentId = 2046;

-- 二级菜单：保安值班
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '保安值班', @parentId, 5, 'duty', 'car/duty/index', NULL, '', 1, 0, 'C', '0', '0', 'car:duty:view', 'log', 'admin', sysdate(), '保安值班页面'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '保安值班' AND parent_id = @parentId);

SET @menuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '保安值班' AND parent_id = @parentId);

-- 按钮：值班操作（出厂/回场）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '值班操作', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:duty:operate', '#', 'admin', sysdate(), '出厂/回场操作权限'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'car:duty:operate');

-- 给保安角色分配权限（假设保安角色ID需要你手动替换）
-- 请根据实际的角色ID修改下面的数字
-- INSERT INTO sys_role_menu (role_id, menu_id)
-- SELECT 角色ID, menu_id FROM sys_menu WHERE perms IN ('car:duty:view', 'car:duty:operate');
