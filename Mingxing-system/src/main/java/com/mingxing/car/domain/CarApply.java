package com.mingxing.car.domain;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

public class CarApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "申请ID", cellType = ColumnType.NUMERIC)
    private Long applyId;

    @Excel(name = "申请单号")
    private String applyNo;

    @Excel(name = "申请人")
    private String applicantName;

    @Excel(name = "申请部门")
    private String deptName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    @Excel(name = "出发地")
    private String startPlace;

    @Excel(name = "目的地")
    private String endPlace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "出发时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date departureTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "预计回归时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date returnTime;

    @Excel(name = "回归时间待定", readConverterExp = "Y=是,N=否")
    private String returnTimeTbd;

    @Excel(name = "单程双程", readConverterExp = "1=单程,2=双程")
    private String tripType;

    @Excel(name = "用车性质", readConverterExp = "1=因公,2=私人")
    private String useType;

    @Excel(name = "用途类型")
    private String purposeType;

    @Excel(name = "用餐司机陪同", readConverterExp = "Y=是,N=否")
    private String driverAccompany;

    @Excel(name = "其他用途说明")
    private String otherPurpose;

    @Excel(name = "联系电话")
    private String contactPhone;

    @Excel(name = "用车事由")
    private String purpose;

    @Excel(name = "车辆ID", cellType = ColumnType.NUMERIC)
    private Long vehicleId;

    private String plateNumber;

    @Excel(name = "司机ID", cellType = ColumnType.NUMERIC)
    private Long driverId;

    private String driverName;

    @Excel(name = "车辆类型", readConverterExp = "1=公司车辆,2=街车车辆")
    private String vehicleType;

    @Excel(name = "街车车牌号")
    private String externalPlateNumber;

    @Excel(name = "街车司机联系方式")
    private String externalDriverPhone;

    @Excel(name = "状态", readConverterExp = "0=待审批,1=已批准,2=已拒绝,3=已结束")
    private String status;

    @Excel(name = "审批意见")
    private String auditRemark;

    private String auditBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    private String statusText;

    private String delFlag;

    private Long hiddenBy;

    @Excel(name = "紧急待补签", readConverterExp = "0=否,1=是")
    private String isUrgent;

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

    public Long getHiddenBy() {
        return hiddenBy;
    }

    public void setHiddenBy(Long hiddenBy) {
        this.hiddenBy = hiddenBy;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    @NotBlank(message = "申请人不能为空")
    @Size(min = 0, max = 50, message = "申请人不能超过50个字符")
    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Size(min = 0, max = 100, message = "出发地不能超过100个字符")
    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    @Size(min = 0, max = 100, message = "目的地不能超过100个字符")
    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnTimeTbd() {
        return returnTimeTbd;
    }

    public void setReturnTimeTbd(String returnTimeTbd) {
        this.returnTimeTbd = returnTimeTbd;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getPurposeType() {
        return purposeType;
    }

    public void setPurposeType(String purposeType) {
        this.purposeType = purposeType;
    }

    public String getDriverAccompany() {
        return driverAccompany;
    }

    public void setDriverAccompany(String driverAccompany) {
        this.driverAccompany = driverAccompany;
    }

    public String getOtherPurpose() {
        return otherPurpose;
    }

    public void setOtherPurpose(String otherPurpose) {
        this.otherPurpose = otherPurpose;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Size(min = 0, max = 200, message = "用车事由不能超过200个字符")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

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

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getExternalPlateNumber() {
        return externalPlateNumber;
    }

    public void setExternalPlateNumber(String externalPlateNumber) {
        this.externalPlateNumber = externalPlateNumber;
    }

    public String getExternalDriverPhone() {
        return externalDriverPhone;
    }

    public void setExternalDriverPhone(String externalDriverPhone) {
        this.externalDriverPhone = externalDriverPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applyId", getApplyId())
                .append("applyNo", getApplyNo())
                .append("applicantName", getApplicantName())
                .append("deptName", getDeptName())
                .append("applyDate", getApplyDate())
                .append("startPlace", getStartPlace())
                .append("endPlace", getEndPlace())
                .append("departureTime", getDepartureTime())
                .append("returnTime", getReturnTime())
                .append("returnTimeTbd", getReturnTimeTbd())
                .append("tripType", getTripType())
                .append("useType", getUseType())
                .append("purposeType", getPurposeType())
                .append("driverAccompany", getDriverAccompany())
                .append("otherPurpose", getOtherPurpose())
                .append("contactPhone", getContactPhone())
                .append("purpose", getPurpose())
                .append("vehicleId", getVehicleId())
                .append("driverId", getDriverId())
                .append("status", getStatus())
                .append("auditRemark", getAuditRemark())
                .append("auditBy", getAuditBy())
                .append("auditTime", getAuditTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}