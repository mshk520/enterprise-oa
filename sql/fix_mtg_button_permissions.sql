-- ============================================
-- 修复会议模块按钮权限（按钮类型缺失导致接口403）
-- 执行后相关用户必须退出重新登录！
-- ============================================

-- 第一步：查看当前会议相关菜单结构（诊断用）
SELECT menu_id, menu_name, parent_id, menu_type, perms, path, component
FROM sys_menu
WHERE menu_id = 2001
   OR parent_id = 2001
   OR perms LIKE 'mtg:%'
ORDER BY menu_id;

-- ============================================
-- 第二步：为「会议室管理」创建按钮权限
-- ============================================

-- 先找到会议室管理菜单的ID
SET @roomMenuId = (SELECT menu_id FROM sys_menu WHERE perms = 'mtg:room:list' AND menu_type = 'C' LIMIT 1);
-- 如果找不到，尝试用路径找
SET @roomMenuId = IFNULL(@roomMenuId, (SELECT menu_id FROM sys_menu WHERE path = 'room' AND parent_id = 2001 LIMIT 1));
-- 如果还找不到，用组件路径找
SET @roomMenuId = IFNULL(@roomMenuId, (SELECT menu_id FROM sys_menu WHERE component = 'mtg/room/index' LIMIT 1));

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
('会议室查询', @roomMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:query', '#', 'admin', sysdate(), NULL),
('会议室新增', @roomMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:add', '#', 'admin', sysdate(), NULL),
('会议室修改', @roomMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:edit', '#', 'admin', sysdate(), NULL),
('会议室删除', @roomMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:remove', '#', 'admin', sysdate(), NULL),
('会议室导出', @roomMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:export', '#', 'admin', sysdate(), NULL),
('修改状态', @roomMenuId, 6, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:status', '#', 'admin', sysdate(), NULL),
('会议室维护', @roomMenuId, 7, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:room:maintain', '#', 'admin', sysdate(), NULL);

-- ============================================
-- 第三步：为「预约申请/预约管理」创建按钮权限
-- ============================================

-- 找到预约管理菜单的ID
SET @bookingMenuId = (SELECT menu_id FROM sys_menu WHERE perms = 'mtg:booking:list' AND menu_type = 'C' LIMIT 1);
SET @bookingMenuId = IFNULL(@bookingMenuId, (SELECT menu_id FROM sys_menu WHERE path = 'booking' AND component = 'mtg/booking/index' LIMIT 1));
SET @bookingMenuId = IFNULL(@bookingMenuId, (SELECT menu_id FROM sys_menu WHERE component = 'mtg/booking/index' LIMIT 1));

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
('预约查询', @bookingMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:query', '#', 'admin', sysdate(), NULL),
('预约新增', @bookingMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:add', '#', 'admin', sysdate(), NULL),
('预约修改', @bookingMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:edit', '#', 'admin', sysdate(), NULL),
('预约取消', @bookingMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:cancel', '#', 'admin', sysdate(), NULL),
('预约审核', @bookingMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:audit', '#', 'admin', sysdate(), NULL),
('预约删除', @bookingMenuId, 6, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:booking:remove', '#', 'admin', sysdate(), NULL);

-- ============================================
-- 第四步：为「会议设备」创建按钮权限
-- ============================================

SET @deviceMenuId = (SELECT menu_id FROM sys_menu WHERE perms = 'mtg:device:list' AND menu_type = 'C' LIMIT 1);
SET @deviceMenuId = IFNULL(@deviceMenuId, (SELECT menu_id FROM sys_menu WHERE path = 'device' AND parent_id = 2001 LIMIT 1));
SET @deviceMenuId = IFNULL(@deviceMenuId, (SELECT menu_id FROM sys_menu WHERE component = 'mtg/device/index' LIMIT 1));

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
('设备查询', @deviceMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:query', '#', 'admin', sysdate(), NULL),
('设备新增', @deviceMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:add', '#', 'admin', sysdate(), NULL),
('设备修改', @deviceMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:edit', '#', 'admin', sysdate(), NULL),
('设备删除', @deviceMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:remove', '#', 'admin', sysdate(), NULL),
('设备导出', @deviceMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:export', '#', 'admin', sysdate(), NULL),
('修改状态', @deviceMenuId, 6, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:device:status', '#', 'admin', sysdate(), NULL);

-- ============================================
-- 第五步：为「固定预约」创建按钮权限
-- ============================================

SET @fixedMenuId = (SELECT menu_id FROM sys_menu WHERE perms = 'mtg:fixedBooking:list' AND menu_type = 'C' LIMIT 1);
SET @fixedMenuId = IFNULL(@fixedMenuId, (SELECT menu_id FROM sys_menu WHERE path = 'fixedBooking' AND component = 'mtg/fixedBooking/index' LIMIT 1));
SET @fixedMenuId = IFNULL(@fixedMenuId, (SELECT menu_id FROM sys_menu WHERE component = 'mtg/fixedBooking/index' LIMIT 1));

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
('固定预约查询', @fixedMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:fixedBooking:query', '#', 'admin', sysdate(), NULL),
('固定预约新增', @fixedMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:fixedBooking:add', '#', 'admin', sysdate(), NULL),
('固定预约修改', @fixedMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:fixedBooking:edit', '#', 'admin', sysdate(), NULL),
('固定预约删除', @fixedMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:fixedBooking:remove', '#', 'admin', sysdate(), NULL);

-- ============================================
-- 第六步：为「报表」创建按钮权限
-- ============================================

SET @reportMenuId = (SELECT menu_id FROM sys_menu WHERE component = 'mtg/report/chinaOrder' LIMIT 1);
SET @reportMenuId = IFNULL(@reportMenuId, (SELECT menu_id FROM sys_menu WHERE path = 'chinaOrder' AND perms LIKE 'mtg:report%' LIMIT 1));

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
('报表查询', @reportMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:report:list', '#', 'admin', sysdate(), NULL),
('报表导出', @reportMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'mtg:report:export', '#', 'admin', sysdate(), NULL);

-- ============================================
-- 第七步：把所有 mtg 按钮权限分配给拥有「會議」菜单的角色
-- 用法：把下面的 @targetRoleId 改成你要授权的角色ID
--   role_id=2  = 普通角色
--   role_id=100 = 管理员(adminUser)
--   role_id=1   = 超级管理员(通常不需要，超级管理员默认有全部权限)
-- ============================================

-- ====== 改这里：设置要授权的角色ID ======
SET @targetRoleId = 2;
-- ==========================================

-- 给目标角色分配所有 mtg 按钮权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT @targetRoleId, menu_id FROM sys_menu
WHERE perms LIKE 'mtg:%'
   AND menu_type = 'F';

-- 同时也给目标角色分配父级菜单（如果还没分配的话）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT @targetRoleId, menu_id FROM sys_menu
WHERE menu_id = 2001
   OR parent_id = 2001
   OR perms LIKE 'mtg:%';

-- ============================================
-- 第八步：验证结果
-- ============================================

SELECT m.menu_id, m.menu_name, m.parent_id, m.menu_type, m.perms,
       CASE WHEN rm.role_id IS NOT NULL THEN '已授权' ELSE '未授权' END AS auth_status
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id AND rm.role_id = @targetRoleId
WHERE m.perms LIKE 'mtg:%'
ORDER BY m.perms;
