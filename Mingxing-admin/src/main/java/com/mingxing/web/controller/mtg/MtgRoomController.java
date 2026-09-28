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
import com.mingxing.mtg.domain.MtgRoom;
import com.mingxing.mtg.service.IMtgRoomService;

@RestController
@RequestMapping("/mtg/room")
public class MtgRoomController extends BaseController {

    @Autowired
    private IMtgRoomService roomService;

    @PreAuthorize("@ss.hasPermi('mtg:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(MtgRoom room) {
        startPage();
        List<MtgRoom> list = roomService.selectRoomList(room);
        return getDataTable(list);
    }

    @Log(title = "会议室管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('mtg:room:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, MtgRoom room) {
        List<MtgRoom> list = roomService.selectRoomList(room);
        ExcelUtil<MtgRoom> util = new ExcelUtil<MtgRoom>(MtgRoom.class);
        util.exportExcel(response, list, "会议室数据");
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable Long roomId) {
        return success(roomService.selectRoomWithDevices(roomId));
    }

    @GetMapping("/available")
    public AjaxResult available(MtgRoom room) {
        room.setStatus("0");
        List<MtgRoom> list = roomService.selectRoomList(room);
        return success(list);
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:add')")
    @Log(title = "会议室管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MtgRoom room) {
        if (!roomService.checkRoomNameUnique(room)) {
            return error("新增会议室'" + room.getRoomName() + "'失败，会议室名称已存在");
        }
        room.setCreateBy(getUsername());
        room.setDelFlag("0");
        room.setStatus("0");
        if (room.getBufferTime() == null) {
            room.setBufferTime(0);
        }
        if (room.getNeedApproval() == null) {
            room.setNeedApproval(0);
        }
        if (room.getVisibilityScope() == null) {
            room.setVisibilityScope("0");
        }
        return toAjax(roomService.insertRoom(room));
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:edit')")
    @Log(title = "会议室管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MtgRoom room) {
        if (!roomService.checkRoomNameUnique(room)) {
            return error("修改会议室'" + room.getRoomName() + "'失败，会议室名称已存在");
        }
        room.setUpdateBy(getUsername());
        return toAjax(roomService.updateRoom(room));
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:remove')")
    @Log(title = "会议室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds) {
        return toAjax(roomService.deleteRoomByIds(roomIds));
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:status')")
    @Log(title = "会议室管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MtgRoom room) {
        room.setUpdateBy(getUsername());
        return toAjax(roomService.updateRoomStatus(room));
    }

    @PreAuthorize("@ss.hasPermi('mtg:room:maintain')")
    @Log(title = "会议室管理", businessType = BusinessType.UPDATE)
    @PutMapping("/maintain")
    public AjaxResult maintain(@RequestBody MtgRoom room) {
        room.setUpdateBy(getUsername());
        return toAjax(roomService.updateRoom(room));
    }
}