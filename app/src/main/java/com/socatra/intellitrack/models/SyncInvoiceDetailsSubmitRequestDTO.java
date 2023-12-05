package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SyncInvoiceDetailsSubmitRequestDTO {




    @SerializedName("ProcessorId")
    @Expose
    private String ProcessorId="";


    @SerializedName("DealerId")
    @Expose
    private String DealerId="";



    @SerializedName("Product")
    @Expose
    private String Product="";


    @SerializedName("Quantity")
    @Expose
    private String Quantity="";

    @SerializedName("BatchId")
    @Expose
    private String BatchId="";

    @SerializedName("Vessel")
    @Expose
    private String Vessel="";

    @SerializedName("PortofLoading")
    @Expose
    private String PortofLoading="";

    @SerializedName("PortofDischarge")
    @Expose
    private String PortofDischarge="";

    @SerializedName("POLTiming")
    @Expose
    private String POLTiming="";

    @SerializedName("PODTiming")
    @Expose
    private String PODTiming="";

    @SerializedName("ShippingTo")
    @Expose
    private String ShippingTo="";

    @SerializedName("ShippingAddress")
    @Expose
    private String ShippingAddress="";


    @SerializedName("IsActive")
    @Expose
    private String IsActive = "1";

    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    @SerializedName("CreatedByUserId")
    @Expose
    private String CreatedByUserId;

    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;

    @SerializedName("UpdatedByUserId")
    @Expose
    private String UpdatedByUserId;



    public String getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(String processorId) {
        ProcessorId = processorId;
    }

    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        DealerId = dealerId;
    }




   

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getBatchId() {
        return BatchId;
    }

    public void setBatchId(String batchId) {
        BatchId = batchId;
    }



    public String getVessel() {
        return Vessel;
    }

    public void setVessel(String vessel) {
        this.Vessel = vessel;
    }

    public String getPortofLoading() {
        return PortofLoading;
    }

    public void setPortofLoading(String portofLoading) {
        PortofLoading = portofLoading;
    }

    public String getPortofDischarge() {
        return PortofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        PortofDischarge = portofDischarge;
    }

    public String getPOLTiming() {
        return POLTiming;
    }

    public void setPOLTiming(String pOLTiming) {POLTiming = pOLTiming;}

    public String getpODTiming() {
        return PODTiming;
    }

    public void setpODTiming(String pODTiming) {PODTiming = pODTiming;}

    public String getShippingTo() {
        return ShippingTo;
    }

    public void setShippingTo(String shippingTo) {
        ShippingTo = shippingTo;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {ShippingAddress = shippingAddress;}



    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }
}