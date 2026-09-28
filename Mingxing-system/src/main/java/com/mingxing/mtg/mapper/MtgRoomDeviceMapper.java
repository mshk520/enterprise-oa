package com.mingxing.mtg.mapper;

import java.util.List;
import com.mingxing.mtg.domain.MtgRoomDevice;

public interface MtgRoomDeviceMapper {

    public int batchInsertRoomDevice(List<MtgRoomDevice> list);

    public int deleteRoomDeviceByRoomId(Long roomId);

    public int deleteRoomDeviceByIds(Long[] ids);

    public List<MtgRoomDevice> selectRoomDeviceList(Long roomId);
}