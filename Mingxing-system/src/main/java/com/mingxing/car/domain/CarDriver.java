package com.mingxing.car.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

public class CarDriver extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "司机ID", cellType = ColumnType.NUMERIC)
    private Long driverId;

    @Excel(name = "司机姓名")
    private String driverName;

    @Excel(name = "司机姓名(英文)")
    private String driverNameEn;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "状态", readConverterExp = "0=在职,1=休假")
    private String status;

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @NotBlank(message = "司机姓名不能为空")
    @Size(min = 0, max = 50, message = "司机姓名不能超过50个字符")
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Size(min = 0, max = 50, message = "司机英文名不能超过50个字符")
    public String getDriverNameEn() {
        return driverNameEn;
    }

    public void setDriverNameEn(String driverNameEn) {
        this.driverNameEn = driverNameEn;
    }

    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 20, message = "手机号不能超过20个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                .append("driverId", getDriverId())
                .append("driverName", getDriverName())
                .append("driverNameEn", getDriverNameEn())
                .append("phone", getPhone())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
