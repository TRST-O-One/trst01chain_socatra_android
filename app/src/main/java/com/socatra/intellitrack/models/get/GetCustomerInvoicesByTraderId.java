
package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCustomerInvoicesByTraderId {

    @SerializedName("Month")
    @Expose
    private String month;
    @SerializedName("Customer")
    @Expose
    private String customer;
    @SerializedName("ToCustomerId")
    @Expose
    private Integer toCustomerId;
    @SerializedName("MonthlyVolume")
    @Expose
    private Integer monthlyVolume;
    @SerializedName("TotalInvoices")
    @Expose
    private Integer totalInvoices;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(Integer toCustomerId) {
        this.toCustomerId = toCustomerId;
    }

    public Integer getMonthlyVolume() {
        return monthlyVolume;
    }

    public void setMonthlyVolume(Integer monthlyVolume) {
        this.monthlyVolume = monthlyVolume;
    }

    public Integer getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Integer totalInvoices) {
        this.totalInvoices = totalInvoices;
    }

}
