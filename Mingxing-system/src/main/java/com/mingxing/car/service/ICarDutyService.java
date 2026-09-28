package com.mingxing.car.service;

import java.util.List;
import java.util.Map;
import com.mingxing.car.domain.CarAccessRecord;

public interface ICarDutyService {

    /** 查询在外车辆（出厂未回场） */
    List<CarAccessRecord> selectOutVehicles();

    /** 查询可出厂车辆（空闲 + 有已批准申请的优先） */
    List<Map<String, Object>> selectAvailableVehicles();

    /** 查询紧急出厂车辆（空闲 + 不在外） */
    List<Map<String, Object>> selectEmergencyVehicles();

    /** 出厂操作 */
    int depart(Long vehicleId, Long applyId, String operatorName);

    /** 紧急出厂（自动创建申请单） */
    int emergencyDepart(Long vehicleId, String driverName, String operatorName);

    /** 回场操作 */
    int returnVehicle(Long accessId, String operatorName);
}
