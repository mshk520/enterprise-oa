package com.mingxing.car.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.common.utils.StringUtils;
import com.mingxing.common.utils.spring.WsEventPublisher;
import com.mingxing.car.domain.CarVehicle;
import com.mingxing.car.mapper.CarVehicleMapper;
import com.mingxing.car.service.ICarVehicleService;

@Service
public class CarVehicleServiceImpl implements ICarVehicleService {

    @Autowired
    private CarVehicleMapper vehicleMapper;

    @Override
    public List<CarVehicle> selectVehicleList(CarVehicle vehicle) {
        return vehicleMapper.selectVehicleList(vehicle);
    }

    @Override
    public CarVehicle selectVehicleById(Long vehicleId) {
        return vehicleMapper.selectVehicleById(vehicleId);
    }

    @Override
    public CarVehicle selectVehicleByPlateNumber(String plateNumber) {
        return vehicleMapper.selectVehicleByPlateNumber(plateNumber);
    }

    @Override
    public int insertVehicle(CarVehicle vehicle) {
        if (vehicle.getSeatCount() == null) {
            vehicle.setSeatCount(5);
        }
        if (vehicle.getStatus() == null) {
            vehicle.setStatus("0");
        }
        return vehicleMapper.insertVehicle(vehicle);
    }

    @Override
    public int updateVehicle(CarVehicle vehicle) {
        int rows = vehicleMapper.updateVehicle(vehicle);
        if (rows > 0) {
            broadcastVehicleStatusChange(vehicle.getVehicleId(), vehicle.getStatus());
        }
        return rows;
    }

    @Override
    public int deleteVehicleById(Long vehicleId) {
        return vehicleMapper.deleteVehicleById(vehicleId);
    }

    @Override
    public int deleteVehicleByIds(Long[] vehicleIds) {
        return vehicleMapper.deleteVehicleByIds(vehicleIds);
    }

    private void broadcastVehicleStatusChange(Long vehicleId, String status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("vehicleId", vehicleId);
        payload.put("status", status);
        WsEventPublisher.publish("vehicleStatusChange", payload);
    }
}
