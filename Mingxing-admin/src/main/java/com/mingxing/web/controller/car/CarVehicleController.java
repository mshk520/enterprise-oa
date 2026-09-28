package com.mingxing.web.controller.car;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.core.page.TableDataInfo;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.common.utils.poi.ExcelUtil;
import com.mingxing.car.domain.CarVehicle;
import com.mingxing.car.service.ICarVehicleService;

@RestController
@RequestMapping("/car/vehicle")
public class CarVehicleController extends BaseController {

    @Autowired
    private ICarVehicleService vehicleService;

    @PreAuthorize("@ss.hasPermi('car:vehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarVehicle vehicle) {
        startPage();
        List<CarVehicle> list = vehicleService.selectVehicleList(vehicle);
        return getDataTable(list);
    }

    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('car:vehicle:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarVehicle vehicle) {
        List<CarVehicle> list = vehicleService.selectVehicleList(vehicle);
        ExcelUtil<CarVehicle> util = new ExcelUtil<CarVehicle>(CarVehicle.class);
        util.exportExcel(response, list, "车辆数据");
    }

    @PreAuthorize("@ss.hasPermi('car:vehicle:query')")
    @GetMapping(value = "/{vehicleId}")
    public AjaxResult getInfo(@PathVariable Long vehicleId) {
        return success(vehicleService.selectVehicleById(vehicleId));
    }

    @PreAuthorize("@ss.hasPermi('car:vehicle:add')")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CarVehicle vehicle) {
        CarVehicle exist = vehicleService.selectVehicleByPlateNumber(vehicle.getPlateNumber());
        if (exist != null) {
            return error("车牌号'" + vehicle.getPlateNumber() + "'已存在");
        }
        return toAjax(vehicleService.insertVehicle(vehicle));
    }

    @PreAuthorize("@ss.hasPermi('car:vehicle:edit')")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CarVehicle vehicle) {
        CarVehicle exist = vehicleService.selectVehicleByPlateNumber(vehicle.getPlateNumber());
        if (exist != null && !exist.getVehicleId().equals(vehicle.getVehicleId())) {
            return error("车牌号'" + vehicle.getPlateNumber() + "'已存在");
        }
        return toAjax(vehicleService.updateVehicle(vehicle));
    }

    @PreAuthorize("@ss.hasPermi('car:vehicle:remove')")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{vehicleIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleIds) {
        return toAjax(vehicleService.deleteVehicleByIds(vehicleIds));
    }
}
