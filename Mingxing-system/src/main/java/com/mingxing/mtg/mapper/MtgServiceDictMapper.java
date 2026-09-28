package com.mingxing.mtg.mapper;

import java.util.List;
import com.mingxing.mtg.domain.MtgServiceDict;

public interface MtgServiceDictMapper {

    public List<MtgServiceDict> selectServiceDictList(MtgServiceDict serviceDict);

    public MtgServiceDict selectServiceDictById(Long serviceId);

    public MtgServiceDict selectServiceDictByName(String serviceName);

    public int insertServiceDict(MtgServiceDict serviceDict);

    public int updateServiceDict(MtgServiceDict serviceDict);

    public int deleteServiceDictById(Long serviceId);

    public int deleteServiceDictByIds(Long[] serviceIds);
}