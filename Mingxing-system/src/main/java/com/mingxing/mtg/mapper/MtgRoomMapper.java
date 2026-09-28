package com.mingxing.mtg.mapper;

import java.util.List;
import com.mingxing.mtg.domain.MtgRoom;

public interface MtgRoomMapper {

    public List<MtgRoom> selectRoomList(MtgRoom room);

    public MtgRoom selectRoomById(Long roomId);

    public MtgRoom selectRoomByIdForUpdate(Long roomId);

    public MtgRoom selectRoomWithDevices(Long roomId);

    public MtgRoom selectRoomByName(MtgRoom room);

    public int insertRoom(MtgRoom room);

    public int updateRoom(MtgRoom room);

    public int deleteRoomById(Long roomId);

    public int deleteRoomByIds(Long[] roomIds);

    public List<Long> selectDeviceIdsByRoomId(Long roomId);
}