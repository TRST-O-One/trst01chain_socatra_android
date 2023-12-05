package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInvoiceDetailsForGrnBySubDealerIdandMainDealerId {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("ToDealerId")
    @Expose
    private String toDealerId;
    @SerializedName("FromDealerId")
    @Expose
    private String fromDealerId;
    @SerializedName("SubDealer")
    @Expose
    private String subDealer;
    @SerializedName("MainDealer")
    @Expose
    private String mainDealer;
    @SerializedName("Product")
    @Expose
    private String product;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("ShippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getToDealerId() {
        return toDealerId;
    }

    public void setToDealerId(String toDealerId) {
        this.toDealerId = toDealerId;
    }

    public String getFromDealerId() {
        return fromDealerId;
    }

    public void setFromDealerId(String fromDealerId) {
        this.fromDealerId = fromDealerId;
    }

    public String getSubDealer() {
        return subDealer;
    }

    public void setSubDealer(String subDealer) {
        this.subDealer = subDealer;
    }

    public String getMainDealer() {
        return mainDealer;
    }

    public void setMainDealer(String mainDealer) {
        this.mainDealer = mainDealer;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
