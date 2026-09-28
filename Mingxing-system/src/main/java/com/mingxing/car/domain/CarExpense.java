package com.mingxing.car.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.core.domain.BaseEntity;
import static com.mingxing.common.annotation.Excel.ColumnType;

public class CarExpense extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long expenseId;

    @Excel(name = "车牌号")
    private String plateNumber;

    private Long applyId;

    private Long accessId;

    /** 关联申请单号（非持久化，用于列表展示） */
    private String applyNo;

    @Excel(name = "用车性质", readConverterExp = "1=因公用車,2=私人用車")
    private String useType;

    @Excel(name = "加油金额", cellType = ColumnType.NUMERIC)
    private BigDecimal fuelAmount;

    @Excel(name = "过路及粤通卡", cellType = ColumnType.NUMERIC)
    private BigDecimal tollFee;

    @Excel(name = "维修费", cellType = ColumnType.NUMERIC)
    private BigDecimal maintenanceFee;

    @Excel(name = "换机油", cellType = ColumnType.NUMERIC)
    private BigDecimal oilChange;

    @Excel(name = "换车胎", cellType = ColumnType.NUMERIC)
    private BigDecimal tireChange;

    @Excel(name = "换刹车片", cellType = ColumnType.NUMERIC)
    private BigDecimal brakeChange;

    @Excel(name = "保险及车船税", cellType = ColumnType.NUMERIC)
    private BigDecimal insuranceTax;

    @Excel(name = "电话费", cellType = ColumnType.NUMERIC)
    private BigDecimal phoneFee;

    @Excel(name = "停车费", cellType = ColumnType.NUMERIC)
    private BigDecimal parkingFee;

    @Excel(name = "住宿费", cellType = ColumnType.NUMERIC)
    private BigDecimal hotelFee;

    @Excel(name = "餐费", cellType = ColumnType.NUMERIC)
    private BigDecimal mealFee;

    @Excel(name = "年审", cellType = ColumnType.NUMERIC)
    private BigDecimal annualInspection;

    @Excel(name = "行驶里程", cellType = ColumnType.NUMERIC)
    private BigDecimal mileage;

    @Excel(name = "入油数(升)", cellType = ColumnType.NUMERIC)
    private BigDecimal fuelVolume;

    @Excel(name = "耗油金额/公里", cellType = ColumnType.NUMERIC)
    private BigDecimal fuelPerKm;

    @Excel(name = "耗油/百公里", cellType = ColumnType.NUMERIC)
    private BigDecimal fuelPer100km;

    @Excel(name = "其它费用", cellType = ColumnType.NUMERIC)
    private BigDecimal otherFee;

    private String delFlag;

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public BigDecimal getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(BigDecimal fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public BigDecimal getTollFee() {
        return tollFee;
    }

    public void setTollFee(BigDecimal tollFee) {
        this.tollFee = tollFee;
    }

    public BigDecimal getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(BigDecimal maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public BigDecimal getOilChange() {
        return oilChange;
    }

    public void setOilChange(BigDecimal oilChange) {
        this.oilChange = oilChange;
    }

    public BigDecimal getTireChange() {
        return tireChange;
    }

    public void setTireChange(BigDecimal tireChange) {
        this.tireChange = tireChange;
    }

    public BigDecimal getBrakeChange() {
        return brakeChange;
    }

    public void setBrakeChange(BigDecimal brakeChange) {
        this.brakeChange = brakeChange;
    }

    public BigDecimal getInsuranceTax() {
        return insuranceTax;
    }

    public void setInsuranceTax(BigDecimal insuranceTax) {
        this.insuranceTax = insuranceTax;
    }

    public BigDecimal getPhoneFee() {
        return phoneFee;
    }

    public void setPhoneFee(BigDecimal phoneFee) {
        this.phoneFee = phoneFee;
    }

    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    public BigDecimal getHotelFee() {
        return hotelFee;
    }

    public void setHotelFee(BigDecimal hotelFee) {
        this.hotelFee = hotelFee;
    }

    public BigDecimal getMealFee() {
        return mealFee;
    }

    public void setMealFee(BigDecimal mealFee) {
        this.mealFee = mealFee;
    }

    public BigDecimal getAnnualInspection() {
        return annualInspection;
    }

    public void setAnnualInspection(BigDecimal annualInspection) {
        this.annualInspection = annualInspection;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getFuelVolume() {
        return fuelVolume;
    }

    public void setFuelVolume(BigDecimal fuelVolume) {
        this.fuelVolume = fuelVolume;
    }

    public BigDecimal getFuelPerKm() {
        return fuelPerKm;
    }

    public void setFuelPerKm(BigDecimal fuelPerKm) {
        this.fuelPerKm = fuelPerKm;
    }

    public BigDecimal getFuelPer100km() {
        return fuelPer100km;
    }

    public void setFuelPer100km(BigDecimal fuelPer100km) {
        this.fuelPer100km = fuelPer100km;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}