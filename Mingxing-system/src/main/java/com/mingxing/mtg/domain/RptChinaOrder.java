package com.mingxing.mtg.domain;

import com.mingxing.common.annotation.Excel;
import com.mingxing.common.core.domain.BaseEntity;

public class RptChinaOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "年份")
    private String year;

    @Excel(name = "月份")
    private String month;

    @Excel(name = "客戶名稱")
    private String customerId;

    @Excel(name = "規格")
    private String gauge;

    @Excel(name = "訂單數量")
    private Integer orderQty;

    @Excel(name = "生產公司")
    private String producingCompany;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGauge() {
        return gauge;
    }

    public void setGauge(String gauge) {
        this.gauge = gauge;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public String getProducingCompany() {
        return producingCompany;
    }

    public void setProducingCompany(String producingCompany) {
        this.producingCompany = producingCompany;
    }
}
