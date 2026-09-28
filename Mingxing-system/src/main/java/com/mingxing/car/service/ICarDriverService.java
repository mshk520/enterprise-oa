package com.mingxing.car.service;

import java.util.List;
import com.mingxing.car.domain.CarDriver;

public interface ICarDriverService {

    public List<CarDriver> selectCarDriverList(CarDriver carDriver);

    public CarDriver selectCarDriverById(Long driverId);

    public int insertCarDriver(CarDriver carDriver);

    public int updateCarDriver(CarDriver carDriver);

    public int deleteCarDriverById(Long driverId);

    public int deleteCarDriverByIds(Long[] driverIds);
}
