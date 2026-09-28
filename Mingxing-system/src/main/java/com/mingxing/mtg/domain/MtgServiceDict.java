package com.mingxing.mtg.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mingxing.common.annotation.Excel;
import com.mingxing.common.annotation.Excel.ColumnType;
import com.mingxing.common.core.domain.BaseEntity;

public class MtgServiceDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "服务ID", cellType = ColumnType.NUMERIC)
    private Long serviceId;

    @Excel(name = "服务名称")
    private String serviceName;

    private String serviceNameEn;

    @Excel(name = "图标")
    private String icon;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @Excel(name = "邮件通知", readConverterExp = "0=否,1=是")
    private String emailNotify;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @NotBlank(message = "服务名称不能为空")
    @Size(min = 0, max = 50, message = "服务名称不能超过50个字符")
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
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

    public String getEmailNotify() {
        return emailNotify;
    }

    public void setEmailNotify(String emailNotify) {
        this.emailNotify = emailNotify;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("serviceId", getServiceId())
                .append("serviceName", getServiceName())
                .append("icon", getIcon())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("emailNotify", getEmailNotify())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}