package com.mingxing.web.controller.mtg;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.annotation.RepeatSubmit;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.utils.SecurityUtils;
import com.mingxing.common.core.page.TableDataInfo;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.mtg.domain.MtgBooking;
import com.mingxing.mtg.service.IMtgBookingService;

@RestController
@RequestMapping("/mtg/booking")
public class MtgBookingController extends BaseController {

    @Autowired
    private IMtgBookingService bookingService;

    @PreAuthorize("@ss.hasPermi('mtg:booking:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtgBooking booking) {
        booking.setHiddenBy(SecurityUtils.getUserId());
        if (!SecurityUtils.getLoginUser().getPermissions().contains("mtg:booking:audit")
                && !SecurityUtils.getLoginUser().getPermissions().contains("mtg:booking:edit")
                && !SecurityUtils.getLoginUser().getPermissions().contains("*:*:*")) {
            booking.setBookerId(SecurityUtils.getUserId());
        }
        startPage();
        List<MtgBooking> list = bookingService.selectBookingList(booking);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:query')")
    @GetMapping(value = "/{bookingId}")
    public AjaxResult getInfo(@PathVariable Long bookingId) {
        return success(bookingService.selectBookingById(bookingId));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:calendar')")
    @GetMapping("/calendar")
    public AjaxResult calendar(@RequestParam String date, @RequestParam(required = false) String endDate) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (endDate != null && !endDate.isEmpty()) {
            return success(bookingService.selectBookingByDateRange(sdf.parse(date), sdf.parse(endDate)));
        }
        return success(bookingService.selectBookingByDateRange(sdf.parse(date), sdf.parse(date)));
    }

    @RepeatSubmit(interval = 5000)
    @PreAuthorize("@ss.hasPermi('mtg:booking:add')")
    @Log(title = "会议预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MtgBooking booking) {
        booking.setBookerId(getUserId());
        booking.setDeptId(getDeptId());
        booking.setCreateBy(getUsername());
        if (booking.getBookingStatus() == null || booking.getBookingStatus().isEmpty()) {
            booking.setBookingStatus("0");
        }
        return toAjax(bookingService.insertBookingChecked(booking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:edit')")
    @Log(title = "会议预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MtgBooking booking) {
        if (bookingService.checkTimeConflict(booking)) {
            return AjaxResult.error("该时间段已被预约，请选择其他时间");
        }
        booking.setUpdateBy(getUsername());
        return toAjax(bookingService.updateBooking(booking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:cancel')")
    @Log(title = "会议预约", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel")
    public AjaxResult cancel(@RequestBody MtgBooking booking) {
        booking.setUpdateBy(getUsername());
        return toAjax(bookingService.cancelBooking(booking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:audit')")
    @Log(title = "会议预约", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody MtgBooking booking) {
        booking.setUpdateBy(getUsername());
        return toAjax(bookingService.auditBooking(booking));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:list')")
    @PutMapping("/{bookingId}/hide")
    public AjaxResult hideBooking(@PathVariable Long bookingId) {
        return toAjax(bookingService.updateBookingHidden(bookingId, SecurityUtils.getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('mtg:booking:remove')")
    @Log(title = "会议预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bookingIds}")
    public AjaxResult remove(@PathVariable Long[] bookingIds) {
        return toAjax(bookingService.deleteBookingByIds(bookingIds));
    }
}
