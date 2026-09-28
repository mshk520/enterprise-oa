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
import com.mingxing.car.domain.CarAccessRecord;
import com.mingxing.car.service.ICarAccessRecordService;

@RestController
@RequestMapping("/car/accessRecord")
public class CarAccessRecordController extends BaseController {

    @Autowired
    private ICarAccessRecordService accessRecordService;

    @PreAuthorize("@ss.hasPermi('car:accessRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarAccessRecord accessRecord) {
        startPage();
        List<CarAccessRecord> list = accessRecordService.selectAccessRecordList(accessRecord);
        return getDataTable(list);
    }

    @Log(title = "车辆出入记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('car:accessRecord:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarAccessRecord accessRecord) {
        List<CarAccessRecord> list = accessRecordService.selectAccessRecordList(accessRecord);
        ExcelUtil<CarAccessRecord> util = new ExcelUtil<CarAccessRecord>(CarAccessRecord.class);
        util.exportExcel(response, list, "车辆出入记录");
    }

    @PreAuthorize("@ss.hasPermi('car:accessRecord:query')")
    @GetMapping(value = "/{accessId}")
    public AjaxResult getInfo(@PathVariable Long accessId) {
        return success(accessRecordService.selectAccessRecordById(accessId));
    }

    @PreAuthorize("@ss.hasPermi('car:accessRecord:add')")
    @Log(title = "车辆出入记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CarAccessRecord accessRecord) {
        return toAjax(accessRecordService.insertAccessRecord(accessRecord));
    }

    @PreAuthorize("@ss.hasPermi('car:accessRecord:edit')")
    @Log(title = "车辆出入记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CarAccessRecord accessRecord) {
        return toAjax(accessRecordService.updateAccessRecord(accessRecord));
    }

    @PreAuthorize("@ss.hasPermi('car:accessRecord:remove')")
    @Log(title = "车辆出入记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{accessIds}")
    public AjaxResult remove(@PathVariable Long[] accessIds) {
        return toAjax(accessRecordService.deleteAccessRecordByIds(accessIds));
    }
}