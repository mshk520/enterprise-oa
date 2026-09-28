package com.mingxing.car.mapper;

import java.util.List;
import com.mingxing.car.domain.CarDriver;

public interface CarDriverMapper {

    public List<CarDriver> selectCarDriverList(CarDriver carDriver);

    public CarDriver selectCarDriverById(Long driverId);

    public int insertCarDriver(CarDriver carDriver);

    public int updateCarDriver(CarDriver carDriver);

    public int deleteCarDriverById(Long driverId);

    public int deleteCarDriverByIds(Long[] driverIds);
}
