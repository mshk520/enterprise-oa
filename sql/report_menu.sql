-- ============================================
-- 报表模块菜单修复脚本
-- 先删除旧数据，再重新插入
-- ============================================

-- 删除旧的报表相关菜单（如果存在）
DELETE FROM sys_menu WHERE menu_name = '報表' AND parent_id = 0;
DELETE FROM sys_menu WHERE menu_name = '中國營業接單數';
DELETE FROM sys_menu WHERE perms IN ('mtg:report:list', 'mtg:report:export') AND menu_type = 'F';

-- 父菜单：报表
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('報表', 0, 5, 'report', NULL, 1, 0, 'M', '0', '0', '', 'clipboard', 'admin', sysdate(), '報表模塊');

-- 获取刚插入的父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 子菜单：中國營業接單數
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('中國營業接單數', @parentId, 1, 'chinaOrder', 'mtg/report/chinaOrder', 1, 0, 'C', '0', '0', 'mtg:report:list', 'chart', 'admin', sysdate(), '中國營業接單數報表');

-- 获取刚插入的子菜单ID
SET @childId = LAST_INSERT_ID();

-- 按钮权限：查詢
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('查詢', @childId, 1, '#', '', 1, 0, 'F', '0', '0', 'mtg:report:list', '#', 'admin', sysdate(), '');

-- 按钮权限：導出
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('導出', @childId, 2, '#', '', 1, 0, 'F', '0', '0', 'mtg:report:export', '#', 'admin', sysdate(), '');
