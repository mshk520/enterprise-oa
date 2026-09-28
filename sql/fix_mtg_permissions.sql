-- ============================================
-- 修复会议室模块权限问题
-- 执行前请确认 parent menu 2001 (會議) 已存在
-- ============================================

-- 1. 确保预约申请（日历视图）菜单存在且 is_frame=0
UPDATE sys_menu SET is_frame = 0, visible = '0', status = '0'
WHERE path = 'calendar' AND component = 'mtg/calendar/index';

-- 2. 确保预约管理菜单存在且 is_frame=0
UPDATE sys_menu SET is_frame = 0, visible = '0', status = '0'
WHERE path = 'booking' AND component = 'mtg/booking/index';

-- 3. 确保固定预约菜单存在且 is_frame=0
UPDATE sys_menu SET is_frame = 0, visible = '0', status = '0'
WHERE path = 'fixedBooking' AND component = 'mtg/fixedBooking/index';

-- 4. 查看当前会议室相关菜单
SELECT menu_id, menu_name, parent_id, path, component, perms, is_frame, menu_type, visible
FROM sys_menu
WHERE menu_id = 2001
   OR (parent_id = 2001)
   OR path IN ('calendar', 'booking', 'fixedBooking')
   OR perms LIKE 'mtg:%'
ORDER BY menu_id;

-- 5. 查看普通角色(role_id=2)当前拥有的 mtg 相关菜单
SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms, m.menu_type
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 2
  AND (m.menu_id = 2001 OR m.parent_id = 2001 OR m.perms LIKE 'mtg:%')
ORDER BY rm.menu_id;

-- 6. 确保普通角色拥有所有 mtg 菜单权限
-- 首先找出所有 mtg 相关菜单ID（包括父菜单和子菜单）
-- 然后插入 sys_role_menu（忽略已存在的）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 2, menu_id FROM sys_menu
WHERE menu_id = 2001
   OR parent_id = 2001
   OR perms LIKE 'mtg:%';

-- 7. 验证结果
SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms, m.menu_type, m.is_frame
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 2
  AND (m.menu_id = 2001 OR m.parent_id = 2001 OR m.perms LIKE 'mtg:%')
ORDER BY m.menu_id;
