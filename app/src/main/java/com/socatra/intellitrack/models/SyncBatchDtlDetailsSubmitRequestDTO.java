
package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SyncBatchDtlDetailsSubmitRequestDTO {

    @SerializedName("BatchHdrId")
    @Expose
    private String BatchHdrId = "";

    @SerializedName("GRNId")
    @Expose
    private String GRNId = "";

//    @SerializedName("ProcessorId")
//    @Expose
//    private String ProcessorId = "";
//
//    @SerializedName("DealerId")
//    @Expose
//    private String DealerId = "";
//
//    @SerializedName("FarmerId")
//    @Expose
//    private String FarmerId = "";

    @SerializedName("Quantity")
    @Expose
    private String Quantity = "";


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


    public String getBatchHdrId() {
        return BatchHdrId;
    }

    public void setBatchHdrId(String batchHdrId) {
        BatchHdrId = batchHdrId;
    }


    public String getGRNId() {
        return GRNId;
    }

    public void setGRNId(String grnId) {
        GRNId = grnId;
    }



    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
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
}