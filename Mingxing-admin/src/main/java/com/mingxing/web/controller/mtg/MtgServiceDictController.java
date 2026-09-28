package com.mingxing.web.controller.mtg;

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
import com.mingxing.mtg.domain.MtgServiceDict;
import com.mingxing.mtg.service.IMtgServiceDictService;

@RestController
@RequestMapping("/mtg/service")
public class MtgServiceDictController extends BaseController {

    @Autowired
    private IMtgServiceDictService serviceDictService;

    @PreAuthorize("@ss.hasPermi('mtg:service:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtgServiceDict serviceDict) {
        startPage();
        List<MtgServiceDict> list = serviceDictService.selectServiceDictList(serviceDict);
        return getDataTable(list);
    }

    @GetMapping("/listEnabled")
    public AjaxResult listEnabled() {
        MtgServiceDict query = new MtgServiceDict();
        query.setStatus("0");
        List<MtgServiceDict> list = serviceDictService.selectServiceDictList(query);
        return success(list);
    }

    @Log(title = "服务管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('mtg:service:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, MtgServiceDict serviceDict) {
        List<MtgServiceDict> list = serviceDictService.selectServiceDictList(serviceDict);
        ExcelUtil<MtgServiceDict> util = new ExcelUtil<MtgServiceDict>(MtgServiceDict.class);
        util.exportExcel(response, list, "服务数据");
    }

    @PreAuthorize("@ss.hasPermi('mtg:service:query')")
    @GetMapping(value = "/{serviceId}")
    public AjaxResult getInfo(@PathVariable Long serviceId) {
        return success(serviceDictService.selectServiceDictById(serviceId));
    }

    @PreAuthorize("@ss.hasPermi('mtg:service:add')")
    @Log(title = "服务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MtgServiceDict serviceDict) {
        if (!serviceDictService.checkServiceNameUnique(serviceDict)) {
            return error("新增服务'" + serviceDict.getServiceName() + "'失败，服务名称已存在");
        }
        serviceDict.setCreateBy(getUsername());
        return toAjax(serviceDictService.insertServiceDict(serviceDict));
    }

    @PreAuthorize("@ss.hasPermi('mtg:service:edit')")
    @Log(title = "服务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MtgServiceDict serviceDict) {
        if (!serviceDictService.checkServiceNameUnique(serviceDict)) {
            return error("修改服务'" + serviceDict.getServiceName() + "'失败，服务名称已存在");
        }
        serviceDict.setUpdateBy(getUsername());
        return toAjax(serviceDictService.updateServiceDict(serviceDict));
    }

    @PreAuthorize("@ss.hasPermi('mtg:service:remove')")
    @Log(title = "服务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceIds}")
    public AjaxResult remove(@PathVariable Long[] serviceIds) {
        return toAjax(serviceDictService.deleteServiceDictByIds(serviceIds));
    }

    @PreAuthorize("@ss.hasPermi('mtg:service:edit')")
    @Log(title = "服务管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MtgServiceDict serviceDict) {
        serviceDict.setUpdateBy(getUsername());
        return toAjax(serviceDictService.updateServiceDict(serviceDict));
    }
}