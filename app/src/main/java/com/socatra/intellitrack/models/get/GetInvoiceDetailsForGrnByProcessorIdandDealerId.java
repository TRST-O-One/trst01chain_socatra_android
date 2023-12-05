package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInvoiceDetailsForGrnByProcessorIdandDealerId {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("ToProcessorId")
    @Expose
    private String toProcessorId;
    @SerializedName("FromDealerId")
    @Expose
    private String fromDealerId;
    @SerializedName("ProcessorName")
    @Expose
    private String processorName;
    @SerializedName("Dealer")
    @Expose
    private String dealer;
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

    public String getToProcessorId() {
        return toProcessorId;
    }

    public void setToProcessorId(String toProcessorId) {
        this.toProcessorId = toProcessorId;
    }

    public String getFromDealerId() {
        return fromDealerId;
    }

    public void setFromDealerId(String fromDealerId) {
        this.fromDealerId = fromDealerId;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
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
