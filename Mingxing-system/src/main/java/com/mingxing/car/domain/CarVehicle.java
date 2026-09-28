package com.mingxing.car.domain;

import java.math.BigDecimal;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

public class CarVehicle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "车辆ID", cellType = ColumnType.NUMERIC)
    private Long vehicleId;

    @Excel(name = "车牌号")
    private String plateNumber;

    @Excel(name = "品牌")
    private String brand;

    @Excel(name = "品牌(英文)")
    private String brandEn;

    @Excel(name = "颜色")
    private String color;

    @Excel(name = "颜色(英文)")
    private String colorEn;

    @Excel(name = "座位数", cellType = ColumnType.NUMERIC)
    private Integer seatCount;

    @Excel(name = "归属部门ID", cellType = ColumnType.NUMERIC)
    private Long deptId;

    private String deptName;

    private String deptNameEn;

    @Excel(name = "状态", readConverterExp = "0=空闲,1=已派出,2=维修中,3=已报废")
    private String status;

    @Excel(name = "总里程", cellType = ColumnType.NUMERIC)
    private BigDecimal totalMileage;

    @Excel(name = "删除标记", readConverterExp = "0=存在,2=删除")
    private String delFlag;

    private String statusText;

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandEn() {
        return brandEn;
    }

    public void setBrandEn(String brandEn) {
        this.brandEn = brandEn;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorEn() {
        return colorEn;
    }

    public void setColorEn(String colorEn) {
        this.colorEn = colorEn;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
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

    public String getDeptNameEn() {
        return deptNameEn;
    }

    public void setDeptNameEn(String deptNameEn) {
        this.deptNameEn = deptNameEn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(BigDecimal totalMileage) {
        this.totalMileage = totalMileage;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
