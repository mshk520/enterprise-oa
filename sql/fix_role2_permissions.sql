-- ============================================
-- 修复普通角色(role_id=2)的会议室权限
-- 执行后必须退出重新登录！
-- ============================================

-- 1. 先查看当前普通角色有哪些 mtg 菜单权限
SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms, m.menu_type
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 2
  AND (m.perms LIKE 'mtg:%' OR m.menu_id IN (
    SELECT menu_id FROM sys_menu WHERE parent_id = 2001
  ))
ORDER BY m.menu_id;

-- 2. 插入普通角色缺少的 mtg 菜单权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 2, menu_id FROM sys_menu
WHERE menu_id = 2001
   OR parent_id = 2001
   OR perms LIKE 'mtg:%';

-- 3. 确认 is_frame 必须为 0（内部组件路由）
UPDATE sys_menu SET is_frame = 0
WHERE component LIKE 'mtg/%';

-- 4. 验证结果
SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms, m.menu_type, m.is_frame
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 2
  AND (m.perms LIKE 'mtg:%' OR m.parent_id = 2001 OR m.menu_id = 2001)
ORDER BY m.menu_id;
