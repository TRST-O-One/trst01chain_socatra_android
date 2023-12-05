package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddAllInvoiceDetailsSubmitTable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose (serialize = false, deserialize = false)
    private int id_new;

    public int getId_new() {
        return id_new;
    }

    public void setId_new(int id_new) {
        this.id_new = id_new;
    }

    @SerializedName("Id")
    @Expose
    private int Id;




    @SerializedName("ToProcessorId")
    @Expose
    private String ToProcessorId="";

    //Code for Only Bank
    @SerializedName("FromProcessorId")
    @Expose
    private String FromProcessorId="";



    @SerializedName("FromDealerId")
    @Expose
    private String FromDealerId="";

    @SerializedName("ToDealerId")
    @Expose
    private String ToDealerId="";

    @SerializedName("ToCustomerId")
    @Expose
    private String ToCustomerId="";

    @SerializedName("Product")
    @Expose
    private String Product="";

    @SerializedName("Quantity")
    @Expose
    private String Quantity="";

    @SerializedName("BatchId")
    @Expose
    private String BatchId="";

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

    @SerializedName("POLTiming")
    @Expose
    private Integer POLTiming;

    @SerializedName("PODTiming")
    @Expose
    private Integer PODTiming;



    @SerializedName("POLTiming")
    @Expose
    private String strPOLTiming;

    @SerializedName("PODTiming")
    @Expose
    private String strPODTiming;


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


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

//    public String getPOLTiming() {
//        return POLTiming;
//    }
//
//    public void setPOLTiming(String pOLTiming) {POLTiming = pOLTiming;}
//
//    public String getpODTiming() {
//        return PODTiming;
//    }
//
//    public void setpODTiming(String pODTiming) {PODTiming = pODTiming;}



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

    public Integer getPOLTiming() {
        return POLTiming;
    }

    public void setPOLTiming(Integer POLTiming) {
        this.POLTiming = POLTiming;
    }

    public Integer getPODTiming() {
        return PODTiming;
    }

    public void setPODTiming(Integer PODTiming) {
        this.PODTiming = PODTiming;
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

    //    public String getPODTiming() {
//        return PODTiming;
//    }
//
//    public void setPODTiming(String PODTiming) {
//        this.PODTiming = PODTiming;
//    }
//
//    public String getPOLTiming() {
//        return POLTiming;
//    }
//
//    public void setPOLTiming(String POLTiming) {
//        this.POLTiming = POLTiming;
//    }
}