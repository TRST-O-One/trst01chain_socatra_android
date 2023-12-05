package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInvoicedetails {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("ProcessorId")
    @Expose
    private Integer processorId;
    @SerializedName("ManufacturerName")
    @Expose
    private String manufacturerName;

    @SerializedName("DealerName")
    @Expose
    private String DealerName;
    @SerializedName("Product")
    @Expose
    private String product;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;

    @SerializedName("ShippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ShippingToName")
    @Expose
    private String shippingToName;


    @SerializedName("Vessel")
    @Expose
    private String Vessel;


    @SerializedName("PODTiming")
    @Expose
    private String PODTiming;

    @SerializedName("PortofDischarge")
    @Expose
    private String PortofDischarge;
    @SerializedName("PortofLoading")
    @Expose
    private String PortofLoading;
    @SerializedName("POLTiming")
    @Expose
    private String POLTiming;
    @SerializedName("FromProcessorId")
    @Expose
    private String FromProcessorId;
    @SerializedName("ToCustomerId")
    @Expose
    private String ToCustomerId;

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

    public Integer getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Integer processorId) {
        this.processorId = processorId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String DealerName) {
        this.DealerName = DealerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getShippingToName() {
        return shippingToName;
    }

    public void setShippingToName(String shippingToName) {
        this.shippingToName = shippingToName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getVessel() {
        return Vessel;
    }

    public void setVessel(String vessel) {
        Vessel = vessel;
    }

    public String getPODTiming() {
        return PODTiming;
    }

    public void setPODTiming(String PODTiming) {
        this.PODTiming = PODTiming;
    }

    public String getPortofDischarge() {
        return PortofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        PortofDischarge = portofDischarge;
    }

    public String getPortofLoading() {
        return PortofLoading;
    }

    public void setPortofLoading(String portofLoading) {
        PortofLoading = portofLoading;
    }

    public String getPOLTiming() {
        return POLTiming;
    }

    public void setPOLTiming(String POLTiming) {
        this.POLTiming = POLTiming;
    }

    public String getFromProcessorId() {
        return FromProcessorId;
    }

    public void setFromProcessorId(String fromProcessorId) {
        FromProcessorId = fromProcessorId;
    }

    public String getToCustomerId() {
        return ToCustomerId;
    }

    public void setToCustomerId(String toCustomerId) {
        ToCustomerId = toCustomerId;
    }
}
