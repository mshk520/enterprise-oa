package com.mingxing.car.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.car.domain.CarDriver;
import com.mingxing.car.mapper.CarDriverMapper;
import com.mingxing.car.service.ICarDriverService;

@Service
public class CarDriverServiceImpl implements ICarDriverService {

    @Autowired
    private CarDriverMapper carDriverMapper;

    @Override
    public List<CarDriver> selectCarDriverList(CarDriver carDriver) {
        return carDriverMapper.selectCarDriverList(carDriver);
    }

    @Override
    public CarDriver selectCarDriverById(Long driverId) {
        return carDriverMapper.selectCarDriverById(driverId);
    }

    @Override
    public int insertCarDriver(CarDriver carDriver) {
        return carDriverMapper.insertCarDriver(carDriver);
    }

    @Override
    public int updateCarDriver(CarDriver carDriver) {
        return carDriverMapper.updateCarDriver(carDriver);
    }

    @Override
    public int deleteCarDriverById(Long driverId) {
        return carDriverMapper.deleteCarDriverById(driverId);
    }

    @Override
    public int deleteCarDriverByIds(Long[] driverIds) {
        return carDriverMapper.deleteCarDriverByIds(driverIds);
    }
}
