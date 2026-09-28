package com.mingxing.web.controller.car;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.annotation.RepeatSubmit;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.common.utils.SecurityUtils;
import com.mingxing.car.domain.CarAccessRecord;
import com.mingxing.car.domain.CarDriver;
import com.mingxing.car.service.ICarDriverService;
import com.mingxing.car.service.ICarDutyService;

@RestController
@RequestMapping("/car/duty")
public class CarDutyController extends BaseController {

    @Autowired
    private ICarDutyService carDutyService;

    @Autowired
    private ICarDriverService carDriverService;

    /** 查询在外车辆 */
    @PreAuthorize("@ss.hasPermi('car:duty:view')")
    @GetMapping("/out")
    public AjaxResult outVehicles() {
        List<CarAccessRecord> list = carDutyService.selectOutVehicles();
        return success(list);
    }

    /** 查询可出厂车辆 */
    @PreAuthorize("@ss.hasPermi('car:duty:view')")
    @GetMapping("/available")
    public AjaxResult available() {
        return success(carDutyService.selectAvailableVehicles());
    }

    /** 查询紧急出厂车辆（空闲+不在外） */
    @PreAuthorize("@ss.hasPermi('car:duty:operate')")
    @GetMapping("/emergencyVehicles")
    public AjaxResult emergencyVehicles() {
        return success(carDutyService.selectEmergencyVehicles());
    }

    /** 出厂操作 */
    @RepeatSubmit(interval = 3000)
    @PreAuthorize("@ss.hasPermi('car:duty:operate')")
    @Log(title = "保安值班-出厂", businessType = BusinessType.INSERT)
    @PostMapping("/depart")
    public AjaxResult depart(@RequestBody Map<String, Long> params) {
        Long vehicleId = params.get("vehicleId");
        Long applyId = params.get("applyId");
        if (vehicleId == null) {
            return error("车辆ID不能为空");
        }
        if (applyId == null) {
            return error("申请单ID不能为空");
        }
        try {
            carDutyService.depart(vehicleId, applyId, getUsername());
            return success("出厂成功");
        } catch (RuntimeException e) {
            return error(e.getMessage());
        }
    }

    /** 回场操作 */
    @RepeatSubmit(interval = 3000)
    @PreAuthorize("@ss.hasPermi('car:duty:operate')")
    @Log(title = "保安值班-回场", businessType = BusinessType.UPDATE)
    @PostMapping("/return")
    public AjaxResult returnVehicle(@RequestBody Map<String, Long> params) {
        Long accessId = params.get("accessId");
        if (accessId == null) {
            return error("出入记录ID不能为空");
        }
        try {
            carDutyService.returnVehicle(accessId, getUsername());
            return success("回场成功");
        } catch (RuntimeException e) {
            return error(e.getMessage());
        }
    }

    /** 查询司机列表（紧急出厂用） */
    @PreAuthorize("@ss.hasPermi('car:duty:operate')")
    @GetMapping("/drivers")
    public AjaxResult drivers() {
        CarDriver query = new CarDriver();
        query.setStatus("0");
        return success(carDriverService.selectCarDriverList(query));
    }

    /** 紧急出厂（自动创建申请单） */
    @RepeatSubmit(interval = 3000)
    @PreAuthorize("@ss.hasPermi('car:duty:operate')")
    @Log(title = "保安值班-紧急出厂", businessType = BusinessType.INSERT)
    @PostMapping("/emergencyDepart")
    public AjaxResult emergencyDepart(@RequestBody Map<String, Object> params) {
        Long vehicleId = params.get("vehicleId") != null ? Long.valueOf(params.get("vehicleId").toString()) : null;
        String driverName = params.get("driverName") != null ? params.get("driverName").toString() : "";
        if (vehicleId == null) {
            return error("车辆ID不能为空");
        }
        try {
            carDutyService.emergencyDepart(vehicleId, driverName, getUsername());
            return success("紧急出厂成功");
        } catch (RuntimeException e) {
            return error(e.getMessage());
        }
    }
}
