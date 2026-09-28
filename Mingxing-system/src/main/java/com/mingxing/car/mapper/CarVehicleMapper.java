package com.mingxing.car.mapper;

import java.util.List;
import com.mingxing.car.domain.CarVehicle;

public interface CarVehicleMapper {

    public List<CarVehicle> selectVehicleList(CarVehicle vehicle);

    public CarVehicle selectVehicleById(Long vehicleId);

    public CarVehicle selectVehicleByIdForUpdate(Long vehicleId);

    public CarVehicle selectVehicleByPlateNumber(String plateNumber);

    public int insertVehicle(CarVehicle vehicle);

    public int updateVehicle(CarVehicle vehicle);

    public int deleteVehicleById(Long vehicleId);

    public int deleteVehicleByIds(Long[] vehicleIds);
}
