-- 车辆管理菜单
-- 一级菜单：用车管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('用车管理', 0, 5, 'car', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), '用车管理模块');

SET @parentId = LAST_INSERT_ID();

-- 二级菜单：车辆管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆管理', @parentId, 1, 'vehicle', 'car/vehicle/index', NULL, '', 1, 0, 'C', '0', '0', 'car:vehicle:list', 'component', 'admin', sysdate(), '车辆管理菜单');

SET @menuId = LAST_INSERT_ID();

-- 按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆查询', @menuId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:query', '#', 'admin', sysdate(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆新增', @menuId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:add', '#', 'admin', sysdate(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆修改', @menuId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:edit', '#', 'admin', sysdate(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆删除', @menuId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:remove', '#', 'admin', sysdate(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('车辆导出', @menuId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'car:vehicle:export', '#', 'admin', sysdate(), '');
