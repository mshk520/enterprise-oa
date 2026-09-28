package com.mingxing.mtg.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.common.constant.UserConstants;
import com.mingxing.common.utils.StringUtils;
import com.mingxing.mtg.domain.MtgServiceDict;
import com.mingxing.mtg.mapper.MtgServiceDictMapper;
import com.mingxing.mtg.service.IMtgServiceDictService;

@Service
public class MtgServiceDictServiceImpl implements IMtgServiceDictService {

    @Autowired
    private MtgServiceDictMapper serviceDictMapper;

    @Override
    public List<MtgServiceDict> selectServiceDictList(MtgServiceDict serviceDict) {
        return serviceDictMapper.selectServiceDictList(serviceDict);
    }

    @Override
    public MtgServiceDict selectServiceDictById(Long serviceId) {
        return serviceDictMapper.selectServiceDictById(serviceId);
    }

    @Override
    public MtgServiceDict selectServiceDictByName(String serviceName) {
        return serviceDictMapper.selectServiceDictByName(serviceName);
    }

    @Override
    public int insertServiceDict(MtgServiceDict serviceDict) {
        return serviceDictMapper.insertServiceDict(serviceDict);
    }

    @Override
    public int updateServiceDict(MtgServiceDict serviceDict) {
        return serviceDictMapper.updateServiceDict(serviceDict);
    }

    @Override
    public int deleteServiceDictById(Long serviceId) {
        return serviceDictMapper.deleteServiceDictById(serviceId);
    }

    @Override
    public int deleteServiceDictByIds(Long[] serviceIds) {
        for (Long serviceId : serviceIds) {
            deleteServiceDictById(serviceId);
        }
        return serviceIds.length;
    }

    @Override
    public boolean checkServiceNameUnique(MtgServiceDict serviceDict) {
        Long serviceId = StringUtils.isNull(serviceDict.getServiceId()) ? -1L : serviceDict.getServiceId();
        MtgServiceDict info = serviceDictMapper.selectServiceDictByName(serviceDict.getServiceName());
        if (StringUtils.isNotNull(info) && info.getServiceId().longValue() != serviceId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}