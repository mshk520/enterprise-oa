package com.mingxing.car.service.impl;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mingxing.car.domain.CarAccessRecord;
import com.mingxing.car.domain.CarApply;
import com.mingxing.car.domain.CarVehicle;
import com.mingxing.car.mapper.CarAccessRecordMapper;
import com.mingxing.car.mapper.CarVehicleMapper;
import com.mingxing.car.service.ICarApplyService;
import com.mingxing.car.service.ICarDutyService;
import com.mingxing.common.utils.SecurityUtils;

@Service
public class CarDutyServiceImpl implements ICarDutyService {

    private static final Logger log = LoggerFactory.getLogger(CarDutyServiceImpl.class);

    @Autowired
    private CarAccessRecordMapper accessRecordMapper;

    @Autowired
    private CarVehicleMapper vehicleMapper;

    @Autowired
    private ICarApplyService carApplyService;

    @Override
    public List<CarAccessRecord> selectOutVehicles() {
        return accessRecordMapper.selectOutRecords();
    }

    @Override
    public List<Map<String, Object>> selectAvailableVehicles() {
        // 查询所有已批准但未完成的申请单（排除当前用户隐藏的）
        CarApply applyQuery = new CarApply();
        applyQuery.setStatus("1");
        applyQuery.setHiddenBy(SecurityUtils.getUserId());
        List<CarApply> approvedApplies = carApplyService.selectCarApplyList(applyQuery);

        // 获取在外车辆的vehicleId集合（这些车不能重复出厂）
        List<CarAccessRecord> outRecords = accessRecordMapper.selectOutRecords();
        Set<Long> outVehicleIds = new HashSet<>();
        for (CarAccessRecord r : outRecords) {
            if (r.getVehicleId() != null) {
                outVehicleIds.add(r.getVehicleId());
            }
        }

        // 只返回有已批准申请单且车辆空闲的记录
        List<Map<String, Object>> result = new ArrayList<>();
        for (CarApply a : approvedApplies) {
            if (a.getVehicleId() == null) continue;
            if (outVehicleIds.contains(a.getVehicleId())) continue;

            if (a.getDepartureTime() != null) {
                long departMs = a.getDepartureTime().getTime();
                long nowMs = System.currentTimeMillis();
                long sevenDaysMs = 7L * 24 * 60 * 60 * 1000;
                if (nowMs - departMs > sevenDaysMs) continue;
                if (departMs - nowMs > sevenDaysMs) continue;
            }

            CarVehicle vehicle = vehicleMapper.selectVehicleById(a.getVehicleId());
            if (vehicle == null || !"0".equals(vehicle.getStatus())) continue;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("vehicleId", vehicle.getVehicleId());
            item.put("plateNumber", vehicle.getPlateNumber());
            item.put("brand", vehicle.getBrand());
            item.put("color", vehicle.getColor());
            item.put("applyId", a.getApplyId());
            item.put("applyNo", a.getApplyNo());
            item.put("applicantName", a.getApplicantName());
            item.put("auditBy", a.getAuditBy());
            item.put("driverName", a.getDriverName());
            result.add(item);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectEmergencyVehicles() {
        // 获取在外车辆的vehicleId集合
        List<CarAccessRecord> outRecords = accessRecordMapper.selectOutRecords();
        Set<Long> outVehicleIds = new HashSet<>();
        for (CarAccessRecord r : outRecords) {
            if (r.getVehicleId() != null) {
                outVehicleIds.add(r.getVehicleId());
            }
        }

        // 查询所有空闲车辆（status=0）
        CarVehicle vehicleQuery = new CarVehicle();
        vehicleQuery.setStatus("0");
        List<CarVehicle> idleVehicles = vehicleMapper.selectVehicleList(vehicleQuery);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CarVehicle v : idleVehicles) {
            if (outVehicleIds.contains(v.getVehicleId())) continue;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("vehicleId", v.getVehicleId());
            item.put("plateNumber", v.getPlateNumber());
            item.put("brand", v.getBrand());
            item.put("color", v.getColor());
            result.add(item);
        }

        return result;
    }

    @Override
    @Transactional
    public int depart(Long vehicleId, Long applyId, String operatorName) {
        // 1. 校验申请单
        if (applyId == null) {
            throw new RuntimeException("申请单不能为空");
        }
        CarApply apply = carApplyService.selectCarApplyById(applyId);
        if (apply == null) {
            throw new RuntimeException("申请单不存在");
        }
        if (!"1".equals(apply.getStatus())) {
            throw new RuntimeException("申请单未审批，不能出厂");
        }

        // 2. 锁定车辆（FOR UPDATE 防并发）
        CarVehicle vehicle = vehicleMapper.selectVehicleByIdForUpdate(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("车辆不存在");
        }
        if (!"0".equals(vehicle.getStatus())) {
            throw new RuntimeException("车辆当前状态不允许出厂");
        }

        // 3. 校验车辆没有未回场的记录（防止重复出厂）
        CarAccessRecord outRecord = accessRecordMapper.selectOutRecordByVehicleId(vehicleId);
        if (outRecord != null) {
            throw new RuntimeException("该车辆尚未回场，不能重复出厂");
        }

        // 4. 创建出入记录
        CarAccessRecord record = new CarAccessRecord();
        record.setVehicleId(vehicleId);
        record.setPlateNumber(vehicle.getPlateNumber());
        record.setDriverName(apply.getDriverName() != null ? apply.getDriverName() : "");
        record.setOutTime(new Date());
        record.setOperatorName(operatorName);
        record.setApplyId(applyId);
        record.setCreateBy(operatorName);
        accessRecordMapper.insertAccessRecord(record);

        // 5. 更新车辆状态为已派出
        CarVehicle updateVehicle = new CarVehicle();
        updateVehicle.setVehicleId(vehicleId);
        updateVehicle.setStatus("1");
        updateVehicle.setUpdateBy(operatorName);
        vehicleMapper.updateVehicle(updateVehicle);

        log.info("车辆出厂: vehicleId={}, applyId={}, operator={}", vehicleId, applyId, operatorName);
        return 1;
    }

    @Override
    @Transactional
    public int emergencyDepart(Long vehicleId, String driverName, String operatorName) {
        // 1. 锁定车辆（FOR UPDATE 防并发）
        CarVehicle vehicle = vehicleMapper.selectVehicleByIdForUpdate(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("车辆不存在");
        }
        if (!"0".equals(vehicle.getStatus())) {
            throw new RuntimeException("车辆当前状态不允许出厂");
        }

        // 2. 校验车辆没有未回场的记录
        CarAccessRecord outRecord = accessRecordMapper.selectOutRecordByVehicleId(vehicleId);
        if (outRecord != null) {
            throw new RuntimeException("该车辆尚未回场，不能重复出厂");
        }

        // 3. 自动创建紧急申请单（status=1 已审批，is_urgent=1 待补签）
        CarApply apply = new CarApply();
        apply.setApplyNo(generateApplyNo());
        apply.setApplicantName(operatorName);
        apply.setApplyDate(new Date());
        apply.setDepartureTime(new Date());
        apply.setDriverName(driverName != null ? driverName : "");
        apply.setVehicleId(vehicleId);
        apply.setPlateNumber(vehicle.getPlateNumber());
        apply.setStatus("1");
        apply.setIsUrgent("1");
        apply.setCreateBy(operatorName);
        carApplyService.insertCarApply(apply);

        // 4. 创建出入记录
        CarAccessRecord record = new CarAccessRecord();
        record.setVehicleId(vehicleId);
        record.setPlateNumber(vehicle.getPlateNumber());
        record.setDriverName(driverName != null ? driverName : "");
        record.setOutTime(new Date());
        record.setOperatorName(operatorName);
        record.setApplyId(apply.getApplyId());
        record.setCreateBy(operatorName);
        accessRecordMapper.insertAccessRecord(record);

        // 5. 更新车辆状态为已派出
        CarVehicle updateVehicle = new CarVehicle();
        updateVehicle.setVehicleId(vehicleId);
        updateVehicle.setStatus("1");
        updateVehicle.setUpdateBy(operatorName);
        vehicleMapper.updateVehicle(updateVehicle);

        log.info("紧急出厂: vehicleId={}, applyId={}, operator={}", vehicleId, apply.getApplyId(), operatorName);
        return 1;
    }

    private String generateApplyNo() {
        return "YY" + new java.text.SimpleDateFormat("yyMMddHHmmss").format(new Date())
                + String.format("%03d", new Random().nextInt(1000));
    }

    @Override
    @Transactional
    public int returnVehicle(Long accessId, String operatorName) {
        // 1. 查出入记录
        CarAccessRecord record = accessRecordMapper.selectAccessRecordById(accessId);
        if (record == null) {
            throw new RuntimeException("出入记录不存在");
        }
        if (record.getInTime() != null) {
            throw new RuntimeException("该车辆已回场，请勿重复操作");
        }

        // 2. 更新回场时间
        CarAccessRecord updateRecord = new CarAccessRecord();
        updateRecord.setAccessId(accessId);
        updateRecord.setInTime(new Date());
        updateRecord.setUpdateBy(operatorName);
        accessRecordMapper.updateAccessRecord(updateRecord);

        // 3. 更新车辆状态为空闲
        if (record.getVehicleId() != null) {
            CarVehicle updateVehicle = new CarVehicle();
            updateVehicle.setVehicleId(record.getVehicleId());
            updateVehicle.setStatus("0");
            updateVehicle.setUpdateBy(operatorName);
            vehicleMapper.updateVehicle(updateVehicle);
        }

        // 4. 如果关联了申请单，完结订单
        if (record.getApplyId() != null) {
            CarApply updateApply = new CarApply();
            updateApply.setApplyId(record.getApplyId());
            updateApply.setStatus("3"); // 已结束
            carApplyService.updateCarApply(updateApply);
        }

        log.info("车辆回场: accessId={}, operator={}", accessId, operatorName);
        return 1;
    }
}
