package com.socatra.intellitrack.models.get;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetInvoiceDetailsByCustomerId {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("Vessel")
    @Expose
    private String vessel;
    @SerializedName("PODTiming")
    @Expose
    private String pODTiming;
    @SerializedName("PortofDischarge")
    @Expose
    private String portofDischarge;
    @SerializedName("PortofLoading")
    @Expose
    private String portofLoading;
    @SerializedName("POLTiming")
    @Expose
    private String pOLTiming;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;

    @SerializedName("ProcessorName")
    @Expose
    private String ProcessorName;

    @SerializedName("ProcessorAddress")
    @Expose
    private String ProcessorAddress;




    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Product")
    @Expose
    private String product;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("ToCustomerId")
    @Expose
    private Integer toCustomerId;
    @SerializedName("ShippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getPODTiming() {
        return pODTiming;
    }

    public void setPODTiming(String pODTiming) {
        this.pODTiming = pODTiming;
    }

    public String getPortofDischarge() {
        return portofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        this.portofDischarge = portofDischarge;
    }

    public String getPortofLoading() {
        return portofLoading;
    }

    public void setPortofLoading(String portofLoading) {
        this.portofLoading = portofLoading;
    }

    public String getPOLTiming() {
        return pOLTiming;
    }

    public void setPOLTiming(String pOLTiming) {
        this.pOLTiming = pOLTiming;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(Integer toCustomerId) {
        this.toCustomerId = toCustomerId;
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

    public String getpODTiming() {
        return pODTiming;
    }

    public void setpODTiming(String pODTiming) {
        this.pODTiming = pODTiming;
    }

    public String getpOLTiming() {
        return pOLTiming;
    }

    public void setpOLTiming(String pOLTiming) {
        this.pOLTiming = pOLTiming;
    }

    public String getProcessorName() {
        return ProcessorName;
    }

    public void setProcessorName(String processorName) {
        ProcessorName = processorName;
    }

    public String getProcessorAddress() {
        return ProcessorAddress;
    }

    public void setProcessorAddress(String processorAddress) {
        ProcessorAddress = processorAddress;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}