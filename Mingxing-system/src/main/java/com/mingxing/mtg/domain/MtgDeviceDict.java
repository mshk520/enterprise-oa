package com.mingxing.mtg.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

public class MtgDeviceDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "设备ID", cellType = ColumnType.NUMERIC)
    private Long deviceId;

    @Excel(name = "设备名称")
    private String deviceName;

    private String deviceNameEn;

    @Excel(name = "图标")
    private String icon;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @NotBlank(message = "设备名称不能为空")
    @Size(min = 0, max = 50, message = "设备名称不能超过50个字符")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNameEn() {
        return deviceNameEn;
    }

    public void setDeviceNameEn(String deviceNameEn) {
        this.deviceNameEn = deviceNameEn;
    }

    @Size(min = 0, max = 100, message = "图标不能超过100个字符")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deviceId", getDeviceId())
                .append("deviceName", getDeviceName())
                .append("icon", getIcon())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}