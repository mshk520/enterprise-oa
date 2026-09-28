package com.mingxing.mtg.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.common.constant.UserConstants;
import com.mingxing.common.exception.ServiceException;
import com.mingxing.common.utils.StringUtils;
import com.mingxing.mtg.domain.MtgDeviceDict;
import com.mingxing.mtg.mapper.MtgDeviceDictMapper;
import com.mingxing.mtg.service.IMtgDeviceDictService;

@Service
public class MtgDeviceDictServiceImpl implements IMtgDeviceDictService {

    @Autowired
    private MtgDeviceDictMapper deviceDictMapper;

    @Override
    public List<MtgDeviceDict> selectDeviceDictList(MtgDeviceDict deviceDict) {
        return deviceDictMapper.selectDeviceDictList(deviceDict);
    }

    @Override
    public MtgDeviceDict selectDeviceDictById(Long deviceId) {
        return deviceDictMapper.selectDeviceDictById(deviceId);
    }

    @Override
    public int insertDeviceDict(MtgDeviceDict deviceDict) {
        return deviceDictMapper.insertDeviceDict(deviceDict);
    }

    @Override
    public int updateDeviceDict(MtgDeviceDict deviceDict) {
        return deviceDictMapper.updateDeviceDict(deviceDict);
    }

    @Override
    public int deleteDeviceDictById(Long deviceId) {
        int count = deviceDictMapper.countRoomByDeviceId(deviceId);
        if (count > 0) {
            throw new ServiceException("该设备已被会议室引用，无法删除");
        }
        return deviceDictMapper.deleteDeviceDictById(deviceId);
    }

    @Override
    public int deleteDeviceDictByIds(Long[] deviceIds) {
        for (Long deviceId : deviceIds) {
            deleteDeviceDictById(deviceId);
        }
        return deviceIds.length;
    }

    @Override
    public boolean checkDeviceNameUnique(MtgDeviceDict deviceDict) {
        Long deviceId = StringUtils.isNull(deviceDict.getDeviceId()) ? -1L : deviceDict.getDeviceId();
        MtgDeviceDict info = deviceDictMapper.selectDeviceDictByName(deviceDict.getDeviceName());
        if (StringUtils.isNotNull(info) && info.getDeviceId().longValue() != deviceId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}