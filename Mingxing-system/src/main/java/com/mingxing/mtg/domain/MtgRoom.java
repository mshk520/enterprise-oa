package com.mingxing.mtg.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

public class MtgRoom extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "会议室ID", cellType = ColumnType.NUMERIC)
    private Long roomId;

    @Excel(name = "会议室名称")
    private String roomName;

    private String roomNameEn;

    @Excel(name = "所属部门ID")
    private Long deptId;

    private String deptName;

    @Excel(name = "楼层")
    private String floor;

    private String floorEn;

    @Excel(name = "具体位置")
    private String location;

    @Excel(name = "容纳人数")
    private Integer capacity;

    @Excel(name = "图片URL")
    private String imageUrl;

    @Excel(name = "开放开始时间")
    private String openTime;

    @Excel(name = "开放结束时间")
    private String closeTime;

    @Excel(name = "缓冲分钟数")
    private Integer bufferTime;

    @Excel(name = "状态", readConverterExp = "0=启用,1=停用,2=维护中")
    private String status;

    @Excel(name = "可见范围", readConverterExp = "0=全部,1=本部门,2=分公司")
    private String visibilityScope;

    @Excel(name = "是否需要审批", readConverterExp = "0=否,1=是")
    private Integer needApproval;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "删除标记", readConverterExp = "0=存在,2=删除")
    private String delFlag;

    private List<Long> deviceIds;

    private List<MtgDeviceDict> deviceList;

    private String statusText;

    private String visibilityScopeText;

    private String needApprovalText;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @NotBlank(message = "会议室名称不能为空")
    @Size(min = 0, max = 100, message = "会议室名称不能超过100个字符")
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomNameEn() {
        return roomNameEn;
    }

    public void setRoomNameEn(String roomNameEn) {
        this.roomNameEn = roomNameEn;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Size(min = 0, max = 20, message = "楼层不能超过20个字符")
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloorEn() {
        return floorEn;
    }

    public void setFloorEn(String floorEn) {
        this.floorEn = floorEn;
    }

    @Size(min = 0, max = 200, message = "具体位置不能超过200个字符")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Size(min = 0, max = 500, message = "图片URL不能超过500个字符")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getBufferTime() {
        return bufferTime;
    }

    public void setBufferTime(Integer bufferTime) {
        this.bufferTime = bufferTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibilityScope() {
        return visibilityScope;
    }

    public void setVisibilityScope(String visibilityScope) {
        this.visibilityScope = visibilityScope;
    }

    public Integer getNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(Integer needApproval) {
        this.needApproval = needApproval;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public List<MtgDeviceDict> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<MtgDeviceDict> deviceList) {
        this.deviceList = deviceList;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getVisibilityScopeText() {
        return visibilityScopeText;
    }

    public void setVisibilityScopeText(String visibilityScopeText) {
        this.visibilityScopeText = visibilityScopeText;
    }

    public String getNeedApprovalText() {
        return needApprovalText;
    }

    public void setNeedApprovalText(String needApprovalText) {
        this.needApprovalText = needApprovalText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roomId", getRoomId())
                .append("roomName", getRoomName())
                .append("deptId", getDeptId())
                .append("floor", getFloor())
                .append("location", getLocation())
                .append("capacity", getCapacity())
                .append("imageUrl", getImageUrl())
                .append("openTime", getOpenTime())
                .append("closeTime", getCloseTime())
                .append("bufferTime", getBufferTime())
                .append("status", getStatus())
                .append("visibilityScope", getVisibilityScope())
                .append("needApproval", getNeedApproval())
                .append("sort", getSort())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}