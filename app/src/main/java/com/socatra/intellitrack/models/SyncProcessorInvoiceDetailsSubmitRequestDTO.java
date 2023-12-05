package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SyncProcessorInvoiceDetailsSubmitRequestDTO {

    @SerializedName("ToProcessorId")
    @Expose
    private String ToProcessorId = "";

    //Code for Only Bank
    @SerializedName("FromProcessorId")
    @Expose
    private String FromProcessorId = "";


    @SerializedName("FromDealerId")
    @Expose
    private String FromDealerId = "";

    @SerializedName("ToDealerId")
    @Expose
    private String ToDealerId = "";

    @SerializedName("ToCustomerId")
    @Expose
    private String ToCustomerId = "";

    @SerializedName("Product")
    @Expose
    private String Product = "";

    @SerializedName("Quantity")
    @Expose
    private String Quantity = "";

    @SerializedName("BatchId")
    @Expose
    private String BatchId = "";

//    @SerializedName("Quantity")
//    @Expose
//    private String Quantity="";

    @SerializedName("Vessel")
    @Expose
    private String Vessel;

    @SerializedName("PortofLoading")
    @Expose
    private String PortofLoading;

    @SerializedName("PortofDischarge")
    @Expose
    private String PortofDischarge;



    @SerializedName("ShippingAddress")
    @Expose
    private String ShippingAddress = "";



    @SerializedName("POLTiming")
    @Expose
    private String strPOLTiming;

    @SerializedName("PODTiming")
    @Expose
    private String strPODTiming;

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


    public String getToProcessorId() {
        return ToProcessorId;
    }

    public void setToProcessorId(String toProcessorId) {
        ToProcessorId = toProcessorId;
    }

    public String getFromProcessorId() {
        return FromProcessorId;
    }

    public void setFromProcessorId(String fromProcessorId) {
        FromProcessorId = fromProcessorId;
    }

    public String getFromDealerId() {
        return FromDealerId;
    }

    public void setFromDealerId(String fromDealerId) {
        FromDealerId = fromDealerId;
    }

    public String getToDealerId() {
        return ToDealerId;
    }

    public void setToDealerId(String toDealerId) {
        ToDealerId = toDealerId;
    }

    public String getToCustomerId() {
        return ToCustomerId;
    }

    public void setToCustomerId(String toCustomerId) {
        ToCustomerId = toCustomerId;
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
        Vessel = vessel;
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



    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

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

    public String getStrPOLTiming() {
        return strPOLTiming;
    }

    public void setStrPOLTiming(String strPOLTiming) {
        this.strPOLTiming = strPOLTiming;
    }

    public String getStrPODTiming() {
        return strPODTiming;
    }

    public void setStrPODTiming(String strPODTiming) {
        this.strPODTiming = strPODTiming;
    }
}