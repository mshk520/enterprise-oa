package com.mingxing.car.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.core.domain.BaseEntity;

public class CarAccessRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "记录ID", cellType = Excel.ColumnType.NUMERIC)
    private Long accessId;

    @Excel(name = "车牌号")
    private String plateNumber;

    @Excel(name = "司机姓名")
    private String driverName;

    @Excel(name = "司机姓名(英文)")
    private String driverNameEn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "回厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inTime;

    @Excel(name = "登记人")
    private String operatorName;

    @Excel(name = "登记人(英文)")
    private String operatorNameEn;

    @Excel(name = "关联申请单ID", cellType = Excel.ColumnType.NUMERIC)
    private Long applyId;

    @Excel(name = "关联车辆ID", cellType = Excel.ColumnType.NUMERIC)
    private Long vehicleId;

    /** 关联申请单号（非持久化，用于列表展示） */
    private String applyNo;

    /** 是否紧急待补签（0否 1是） */
    private String isUrgent;

    @Excel(name = "删除标记", readConverterExp = "0=存在,2=删除")
    private String delFlag;

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNameEn() {
        return driverNameEn;
    }

    public void setDriverNameEn(String driverNameEn) {
        this.driverNameEn = driverNameEn;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorNameEn() {
        return operatorNameEn;
    }

    public void setOperatorNameEn(String operatorNameEn) {
        this.operatorNameEn = operatorNameEn;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
