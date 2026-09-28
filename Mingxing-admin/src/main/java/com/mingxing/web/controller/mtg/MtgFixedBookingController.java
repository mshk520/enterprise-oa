package com.mingxing.web.controller.mtg;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.core.page.TableDataInfo;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.mtg.domain.MtgFixedBooking;
import com.mingxing.mtg.service.IMtgFixedBookingService;

@RestController
@RequestMapping("/mtg/fixedBooking")
public class MtgFixedBookingController extends BaseController {

    @Autowired
    private IMtgFixedBookingService fixedBookingService;

    @PreAuthorize("@ss.hasPermi('mtg:fixedBooking:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtgFixedBooking fixedBooking) {
        if (!isSuperAdmin()) {
            fixedBooking.setBookerId(getUserId());
        }
        startPage();
        List<MtgFixedBooking> list = fixedBookingService.selectFixedBookingList(fixedBooking);
        return getDataTable(list);
    }

    private boolean isSuperAdmin() {
        return getUsername() != null && getUsername().equals("admin");
    }

    @PreAuthorize("@ss.hasPermi('mtg:fixedBooking:query')")
    @GetMapping(value = "/{fixedId}")
    public AjaxResult getInfo(@PathVariable Long fixedId) {
        return success(fixedBookingService.selectFixedBookingById(fixedId));
    }

    @PreAuthorize("@ss.hasPermi('mtg:fixedBooking:add')")
    @Log(title = "固定预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MtgFixedBooking fixedBooking) {
        fixedBooking.setBookerId(getUserId());
        fixedBooking.setDeptId(getDeptId());
        fixedBooking.setCreateBy(getUsername());
        return toAjax(fixedBookingService.insertFixedBooking(fixedBooking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:fixedBooking:edit')")
    @Log(title = "固定预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MtgFixedBooking fixedBooking) {
        fixedBooking.setUpdateBy(getUsername());
        return toAjax(fixedBookingService.updateFixedBooking(fixedBooking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:fixedBooking:remove')")
    @Log(title = "固定预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fixedIds}")
    public AjaxResult remove(@PathVariable Long[] fixedIds) {
        return toAjax(fixedBookingService.deleteFixedBookingByIds(fixedIds));
    }
}
