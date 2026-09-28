package com.mingxing.car.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mingxing.common.utils.spring.WsEventPublisher;
import com.mingxing.car.domain.CarApply;
import com.mingxing.car.mapper.CarApplyMapper;
import com.mingxing.car.service.ICarApplyService;

@Service
public class CarApplyServiceImpl implements ICarApplyService {

    @Autowired
    private CarApplyMapper carApplyMapper;

    @Override
    public List<CarApply> selectCarApplyList(CarApply carApply) {
        return carApplyMapper.selectCarApplyList(carApply);
    }

    @Override
    public CarApply selectCarApplyById(Long applyId) {
        return carApplyMapper.selectCarApplyById(applyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCarApply(CarApply carApply) {
        int rows = carApplyMapper.insertCarApply(carApply);
        if (rows > 0 && carApply.getApplyId() != null) {
            broadcastCarApplyChange("create", carApply);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCarApply(CarApply carApply) {
        int rows = carApplyMapper.updateCarApply(carApply);
        if (rows > 0) {
            broadcastCarApplyChange("update", carApply);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCarApplyById(Long applyId) {
        return carApplyMapper.deleteCarApplyById(applyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCarApplyByIds(Long[] applyIds) {
        return carApplyMapper.deleteCarApplyByIds(applyIds);
    }

    private void broadcastCarApplyChange(String action, CarApply apply) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);
        payload.put("applyId", apply.getApplyId());
        payload.put("vehicleId", apply.getVehicleId());
        payload.put("applicantName", apply.getApplicantName());
        payload.put("status", apply.getStatus());
        WsEventPublisher.publish("carApplyChange", payload);
    }

    @Override
    public int updateApplyHidden(Long applyId, Long hiddenBy) {
        return carApplyMapper.updateApplyHidden(applyId, hiddenBy);
    }
}
