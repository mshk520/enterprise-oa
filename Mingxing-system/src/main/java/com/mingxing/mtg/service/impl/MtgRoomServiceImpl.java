package com.mingxing.mtg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mingxing.common.constant.UserConstants;
import com.mingxing.common.exception.ServiceException;
import com.mingxing.common.utils.StringUtils;
import com.mingxing.common.utils.spring.WsEventPublisher;
import com.mingxing.mtg.domain.MtgDeviceDict;
import com.mingxing.mtg.domain.MtgRoom;
import com.mingxing.mtg.domain.MtgRoomDevice;
import com.mingxing.mtg.mapper.MtgDeviceDictMapper;
import com.mingxing.mtg.mapper.MtgRoomDeviceMapper;
import com.mingxing.mtg.mapper.MtgRoomMapper;
import com.mingxing.mtg.service.IMtgRoomService;

@Service
public class MtgRoomServiceImpl implements IMtgRoomService {

    @Autowired
    private MtgRoomMapper roomMapper;

    @Autowired
    private MtgRoomDeviceMapper roomDeviceMapper;

    @Autowired
    private MtgDeviceDictMapper deviceDictMapper;

    @Override
    public List<MtgRoom> selectRoomList(MtgRoom room) {
        List<MtgRoom> roomList = roomMapper.selectRoomList(room);
        for (MtgRoom r : roomList) {
            r.setStatusText(getStatusText(r.getStatus()));
            r.setVisibilityScopeText(getVisibilityScopeText(r.getVisibilityScope()));
            r.setNeedApprovalText(getNeedApprovalText(r.getNeedApproval()));
            List<Long> deviceIds = roomMapper.selectDeviceIdsByRoomId(r.getRoomId());
            if (deviceIds != null && !deviceIds.isEmpty()) {
                List<MtgDeviceDict> deviceList = new ArrayList<>();
                for (Long deviceId : deviceIds) {
                    MtgDeviceDict device = deviceDictMapper.selectDeviceDictById(deviceId);
                    if (device != null) {
                        deviceList.add(device);
                    }
                }
                r.setDeviceList(deviceList);
            }
        }
        return roomList;
    }

    @Override
    public MtgRoom selectRoomById(Long roomId) {
        MtgRoom room = roomMapper.selectRoomById(roomId);
        if (room != null) {
            room.setStatusText(getStatusText(room.getStatus()));
            room.setVisibilityScopeText(getVisibilityScopeText(room.getVisibilityScope()));
            room.setNeedApprovalText(getNeedApprovalText(room.getNeedApproval()));
        }
        return room;
    }

    @Override
    public MtgRoom selectRoomByIdForUpdate(Long roomId) {
        return roomMapper.selectRoomByIdForUpdate(roomId);
    }

    @Override
    public MtgRoom selectRoomWithDevices(Long roomId) {
        MtgRoom room = roomMapper.selectRoomWithDevices(roomId);
        if (room != null) {
            room.setStatusText(getStatusText(room.getStatus()));
            room.setVisibilityScopeText(getVisibilityScopeText(room.getVisibilityScope()));
            room.setNeedApprovalText(getNeedApprovalText(room.getNeedApproval()));

            List<Long> deviceIds = roomMapper.selectDeviceIdsByRoomId(roomId);
            room.setDeviceIds(deviceIds);

            if (deviceIds != null && !deviceIds.isEmpty()) {
                List<MtgDeviceDict> deviceList = new ArrayList<>();
                for (Long deviceId : deviceIds) {
                    MtgDeviceDict device = deviceDictMapper.selectDeviceDictById(deviceId);
                    if (device != null) {
                        deviceList.add(device);
                    }
                }
                room.setDeviceList(deviceList);
            }
        }
        return room;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRoom(MtgRoom room) {
        int rows = roomMapper.insertRoom(room);

        if (StringUtils.isNotEmpty(room.getDeviceIds())) {
            List<MtgRoomDevice> roomDevices = new ArrayList<>();
            for (Long deviceId : room.getDeviceIds()) {
                MtgRoomDevice roomDevice = new MtgRoomDevice();
                roomDevice.setRoomId(room.getRoomId());
                roomDevice.setDeviceId(deviceId);
                roomDevice.setCreateBy(room.getCreateBy());
                roomDevices.add(roomDevice);
            }
            roomDeviceMapper.batchInsertRoomDevice(roomDevices);
        }

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRoom(MtgRoom room) {
        int rows = roomMapper.updateRoom(room);

        if (StringUtils.isNotEmpty(room.getDeviceIds())) {
            roomDeviceMapper.deleteRoomDeviceByRoomId(room.getRoomId());

            List<MtgRoomDevice> roomDevices = new ArrayList<>();
            for (Long deviceId : room.getDeviceIds()) {
                MtgRoomDevice roomDevice = new MtgRoomDevice();
                roomDevice.setRoomId(room.getRoomId());
                roomDevice.setDeviceId(deviceId);
                roomDevice.setCreateBy(room.getUpdateBy());
                roomDevices.add(roomDevice);
            }
            roomDeviceMapper.batchInsertRoomDevice(roomDevices);
        }

        broadcastRoomStatusChange(room.getRoomId(), room.getStatus());
        return rows;
    }

    @Override
    public int deleteRoomById(Long roomId) {
        roomDeviceMapper.deleteRoomDeviceByRoomId(roomId);
        return roomMapper.deleteRoomById(roomId);
    }

    @Override
    public int deleteRoomByIds(Long[] roomIds) {
        for (Long roomId : roomIds) {
            deleteRoomById(roomId);
        }
        return roomIds.length;
    }

    @Override
    public boolean checkRoomNameUnique(MtgRoom room) {
        MtgRoom info = roomMapper.selectRoomByName(room);
        if (StringUtils.isNotNull(info)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int updateRoomStatus(MtgRoom room) {
        int rows = roomMapper.updateRoom(room);
        broadcastRoomStatusChange(room.getRoomId(), room.getStatus());
        return rows;
    }

    @Override
    public int maintainRoom(Long roomId) {
        MtgRoom room = new MtgRoom();
        room.setRoomId(roomId);
        room.setStatus("2");
        int rows = roomMapper.updateRoom(room);
        broadcastRoomStatusChange(roomId, "2");
        return rows;
    }

    private void broadcastRoomStatusChange(Long roomId, String status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("roomId", roomId);
        payload.put("status", status);
        WsEventPublisher.publish("roomStatusChange", payload);
    }

    private String getStatusText(String status) {
        if ("0".equals(status)) {
            return "启用";
        } else if ("1".equals(status)) {
            return "停用";
        } else if ("2".equals(status)) {
            return "维护中";
        }
        return "";
    }

    private String getVisibilityScopeText(String scope) {
        if ("1".equals(scope)) {
            return "本部门";
        } else if ("2".equals(scope)) {
            return "分公司";
        } else if ("0".equals(scope)) {
            return "全部";
        }
        return "";
    }

    private String getNeedApprovalText(Integer needApproval) {
        if (needApproval != null && needApproval == 1) {
            return "是";
        }
        return "否";
    }
}
