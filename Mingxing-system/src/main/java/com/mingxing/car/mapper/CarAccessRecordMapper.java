package com.mingxing.car.mapper;

import java.util.List;
import com.mingxing.car.domain.CarAccessRecord;

public interface CarAccessRecordMapper {

    public List<CarAccessRecord> selectAccessRecordList(CarAccessRecord accessRecord);

    public CarAccessRecord selectAccessRecordById(Long accessId);

    public CarAccessRecord selectAccessRecordByPlateNumber(String plateNumber);

    public int insertAccessRecord(CarAccessRecord accessRecord);

    public int updateAccessRecord(CarAccessRecord accessRecord);

    public int deleteAccessRecordById(Long accessId);

    public int deleteAccessRecordByIds(Long[] accessIds);

    /** 查询在外车辆（出厂未回场） */
    public List<CarAccessRecord> selectOutRecords();

    /** 查询指定车辆未回场的记录（用于出厂校验） */
    public CarAccessRecord selectOutRecordByVehicleId(Long vehicleId);
}