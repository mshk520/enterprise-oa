package com.mingxing.car.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.car.domain.CarAccessRecord;
import com.mingxing.car.mapper.CarAccessRecordMapper;
import com.mingxing.car.service.ICarAccessRecordService;

@Service
public class CarAccessRecordServiceImpl implements ICarAccessRecordService {

    @Autowired
    private CarAccessRecordMapper accessRecordMapper;

    @Override
    public List<CarAccessRecord> selectAccessRecordList(CarAccessRecord accessRecord) {
        return accessRecordMapper.selectAccessRecordList(accessRecord);
    }

    @Override
    public CarAccessRecord selectAccessRecordById(Long accessId) {
        return accessRecordMapper.selectAccessRecordById(accessId);
    }

    @Override
    public CarAccessRecord selectAccessRecordByPlateNumber(String plateNumber) {
        return accessRecordMapper.selectAccessRecordByPlateNumber(plateNumber);
    }

    @Override
    public int insertAccessRecord(CarAccessRecord accessRecord) {
        return accessRecordMapper.insertAccessRecord(accessRecord);
    }

    @Override
    public int updateAccessRecord(CarAccessRecord accessRecord) {
        return accessRecordMapper.updateAccessRecord(accessRecord);
    }

    @Override
    public int deleteAccessRecordById(Long accessId) {
        return accessRecordMapper.deleteAccessRecordById(accessId);
    }

    @Override
    public int deleteAccessRecordByIds(Long[] accessIds) {
        return accessRecordMapper.deleteAccessRecordByIds(accessIds);
    }
}