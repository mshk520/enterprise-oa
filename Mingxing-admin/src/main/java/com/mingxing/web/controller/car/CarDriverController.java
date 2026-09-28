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
import com.mingxing.car.domain.CarDriver;
import com.mingxing.car.service.ICarDriverService;

@RestController
@RequestMapping("/car/driver")
public class CarDriverController extends BaseController {

    @Autowired
    private ICarDriverService carDriverService;

    @PreAuthorize("@ss.hasPermi('car:driver:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarDriver carDriver) {
        startPage();
        List<CarDriver> list = carDriverService.selectCarDriverList(carDriver);
        return getDataTable(list);
    }

    @Log(title = "司机信息", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('car:driver:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarDriver carDriver) {
        List<CarDriver> list = carDriverService.selectCarDriverList(carDriver);
        ExcelUtil<CarDriver> util = new ExcelUtil<CarDriver>(CarDriver.class);
        util.exportExcel(response, list, "司机数据");
    }

    @PreAuthorize("@ss.hasPermi('car:driver:query')")
    @GetMapping(value = "/{driverId}")
    public AjaxResult getInfo(@PathVariable Long driverId) {
        return success(carDriverService.selectCarDriverById(driverId));
    }

    @PreAuthorize("@ss.hasPermi('car:driver:add')")
    @Log(title = "司机信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CarDriver carDriver) {
        carDriver.setCreateBy(getUsername());
        return toAjax(carDriverService.insertCarDriver(carDriver));
    }

    @PreAuthorize("@ss.hasPermi('car:driver:edit')")
    @Log(title = "司机信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CarDriver carDriver) {
        carDriver.setUpdateBy(getUsername());
        return toAjax(carDriverService.updateCarDriver(carDriver));
    }

    @PreAuthorize("@ss.hasPermi('car:driver:remove')")
    @Log(title = "司机信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{driverIds}")
    public AjaxResult remove(@PathVariable Long[] driverIds) {
        return toAjax(carDriverService.deleteCarDriverByIds(driverIds));
    }
}
