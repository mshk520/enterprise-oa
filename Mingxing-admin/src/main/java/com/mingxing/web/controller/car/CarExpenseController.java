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
import com.mingxing.car.domain.CarExpense;
import com.mingxing.car.service.ICarExpenseService;

@RestController
@RequestMapping("/car/expense")
public class CarExpenseController extends BaseController {

    @Autowired
    private ICarExpenseService carExpenseService;

    @PreAuthorize("@ss.hasPermi('car:expense:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarExpense carExpense) {
        startPage();
        List<CarExpense> list = carExpenseService.selectCarExpenseList(carExpense);
        return getDataTable(list);
    }

    @Log(title = "车辆费用", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('car:expense:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarExpense carExpense) {
        List<CarExpense> list = carExpenseService.selectCarExpenseList(carExpense);
        ExcelUtil<CarExpense> util = new ExcelUtil<CarExpense>(CarExpense.class);
        util.exportExcel(response, list, "车辆费用数据");
    }

    @PreAuthorize("@ss.hasPermi('car:expense:query')")
    @GetMapping(value = "/{expenseId}")
    public AjaxResult getInfo(@PathVariable Long expenseId) {
        return success(carExpenseService.selectCarExpenseById(expenseId));
    }

    @PreAuthorize("@ss.hasPermi('car:expense:add')")
    @Log(title = "车辆费用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CarExpense carExpense) {
        carExpense.setCreateBy(getUsername());
        return toAjax(carExpenseService.insertCarExpense(carExpense));
    }

    @PreAuthorize("@ss.hasPermi('car:expense:edit')")
    @Log(title = "车辆费用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CarExpense carExpense) {
        carExpense.setUpdateBy(getUsername());
        return toAjax(carExpenseService.updateCarExpense(carExpense));
    }

    @PreAuthorize("@ss.hasPermi('car:expense:remove')")
    @Log(title = "车辆费用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{expenseIds}")
    public AjaxResult remove(@PathVariable Long[] expenseIds) {
        return toAjax(carExpenseService.deleteCarExpenseByIds(expenseIds));
    }
}