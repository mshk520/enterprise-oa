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
import com.mingxing.mtg.domain.MtgDeviceDict;
import com.mingxing.mtg.service.IMtgDeviceDictService;

@RestController
@RequestMapping("/mtg/device")
public class MtgDeviceDictController extends BaseController {

    @Autowired
    private IMtgDeviceDictService deviceDictService;

    @PreAuthorize("@ss.hasPermi('mtg:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtgDeviceDict deviceDict) {
        startPage();
        List<MtgDeviceDict> list = deviceDictService.selectDeviceDictList(deviceDict);
        return getDataTable(list);
    }

    /**
     * 查询启用的设备列表（无需权限，用于会议室关联设备）
     */
    @GetMapping("/listEnabled")
    public AjaxResult listEnabled() {
        MtgDeviceDict query = new MtgDeviceDict();
        query.setStatus("0");
        List<MtgDeviceDict> list = deviceDictService.selectDeviceDictList(query);
        System.out.println("=== listEnabled 返回设备数量: " + (list == null ? 0 : list.size()));
        return success(list);
    }

    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('mtg:device:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, MtgDeviceDict deviceDict) {
        List<MtgDeviceDict> list = deviceDictService.selectDeviceDictList(deviceDict);
        ExcelUtil<MtgDeviceDict> util = new ExcelUtil<MtgDeviceDict>(MtgDeviceDict.class);
        util.exportExcel(response, list, "设备数据");
    }

    @PreAuthorize("@ss.hasPermi('mtg:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable Long deviceId) {
        return success(deviceDictService.selectDeviceDictById(deviceId));
    }

    @PreAuthorize("@ss.hasPermi('mtg:device:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MtgDeviceDict deviceDict) {
        if (!deviceDictService.checkDeviceNameUnique(deviceDict)) {
            return error("新增设备'" + deviceDict.getDeviceName() + "'失败，设备名称已存在");
        }
        deviceDict.setCreateBy(getUsername());
        return toAjax(deviceDictService.insertDeviceDict(deviceDict));
    }

    @PreAuthorize("@ss.hasPermi('mtg:device:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MtgDeviceDict deviceDict) {
        if (!deviceDictService.checkDeviceNameUnique(deviceDict)) {
            return error("修改设备'" + deviceDict.getDeviceName() + "'失败，设备名称已存在");
        }
        deviceDict.setUpdateBy(getUsername());
        return toAjax(deviceDictService.updateDeviceDict(deviceDict));
    }

    @PreAuthorize("@ss.hasPermi('mtg:device:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds) {
        return toAjax(deviceDictService.deleteDeviceDictByIds(deviceIds));
    }

    @PreAuthorize("@ss.hasPermi('mtg:device:status')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MtgDeviceDict deviceDict) {
        deviceDict.setUpdateBy(getUsername());
        return toAjax(deviceDictService.updateDeviceDict(deviceDict));
    }
}