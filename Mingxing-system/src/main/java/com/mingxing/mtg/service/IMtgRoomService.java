package com.mingxing.mtg.service;

import java.util.List;
import com.mingxing.mtg.domain.MtgRoom;

public interface IMtgRoomService {

    public List<MtgRoom> selectRoomList(MtgRoom room);

    public MtgRoom selectRoomById(Long roomId);

    public MtgRoom selectRoomByIdForUpdate(Long roomId);

    public MtgRoom selectRoomWithDevices(Long roomId);

    public int insertRoom(MtgRoom room);

    public int updateRoom(MtgRoom room);

    public int deleteRoomById(Long roomId);

    public int deleteRoomByIds(Long[] roomIds);

    public boolean checkRoomNameUnique(MtgRoom room);

    public int updateRoomStatus(MtgRoom room);

    public int maintainRoom(Long roomId);
}