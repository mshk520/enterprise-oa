package com.mingxing.mtg.mapper;

import java.util.List;
import com.mingxing.mtg.domain.MtgDeviceDict;

public interface MtgDeviceDictMapper {

    public List<MtgDeviceDict> selectDeviceDictList(MtgDeviceDict deviceDict);

    public MtgDeviceDict selectDeviceDictById(Long deviceId);

    public MtgDeviceDict selectDeviceDictByName(String deviceName);

    public int insertDeviceDict(MtgDeviceDict deviceDict);

    public int updateDeviceDict(MtgDeviceDict deviceDict);

    public int deleteDeviceDictById(Long deviceId);

    public int deleteDeviceDictByIds(Long[] deviceIds);

    public int countRoomByDeviceId(Long deviceId);
}