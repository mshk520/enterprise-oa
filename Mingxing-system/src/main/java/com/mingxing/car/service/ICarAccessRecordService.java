package com.mingxing.car.service;

import java.util.List;
import com.mingxing.car.domain.CarAccessRecord;

public interface ICarAccessRecordService {

    public List<CarAccessRecord> selectAccessRecordList(CarAccessRecord accessRecord);

    public CarAccessRecord selectAccessRecordById(Long accessId);

    public CarAccessRecord selectAccessRecordByPlateNumber(String plateNumber);

    public int insertAccessRecord(CarAccessRecord accessRecord);

    public int updateAccessRecord(CarAccessRecord accessRecord);

    public int deleteAccessRecordById(Long accessId);

    public int deleteAccessRecordByIds(Long[] accessIds);
}