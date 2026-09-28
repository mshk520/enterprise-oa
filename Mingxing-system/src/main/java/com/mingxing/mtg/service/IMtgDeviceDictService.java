package com.mingxing.mtg.service;

import java.util.List;
import com.mingxing.mtg.domain.MtgDeviceDict;

public interface IMtgDeviceDictService {

    public List<MtgDeviceDict> selectDeviceDictList(MtgDeviceDict deviceDict);

    public MtgDeviceDict selectDeviceDictById(Long deviceId);

    public int insertDeviceDict(MtgDeviceDict deviceDict);

    public int updateDeviceDict(MtgDeviceDict deviceDict);

    public int deleteDeviceDictById(Long deviceId);

    public int deleteDeviceDictByIds(Long[] deviceIds);

    public boolean checkDeviceNameUnique(MtgDeviceDict deviceDict);
}