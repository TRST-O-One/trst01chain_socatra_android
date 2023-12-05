package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity
public class AddBatchProcessingDtlDetailsSubmitTable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
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

    @SerializedName("BatchHdrId")
    @Expose
    private String BatchHdrId = "";

    @SerializedName("GRNId")
    @Expose
    private String GRNId = "";

    @SerializedName("ProcessorId")
    @Expose
    private String ProcessorId = "";

    @SerializedName("DealerId")
    @Expose
    private String DealerId = "";

    @SerializedName("FarmerId")
    @Expose
    private String FarmerId = "";

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

    public void setGRNId(String gRNId) {
        GRNId = gRNId;
    }


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

    public String getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(String farmerId) {
        FarmerId = farmerId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
