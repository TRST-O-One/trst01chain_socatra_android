package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetSubDealerDataFromServer {
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

    @SerializedName("DealerId")
    @Expose
    private String DealerId;

    @SerializedName("DealerName")
    @Expose
    private String DealerName;

    @SerializedName("SubDealerId")
    @Expose
    private String SubDealerId;

    @SerializedName("SubDealerName")
    @Expose
    private String SubDealerName;

    @SerializedName("SubDealerAddress")
    @Expose
    private String SubDealerAddress;




    @SerializedName("IsActive")
    @Expose
    private String IsActive = "true";

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

    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        DealerId = dealerId;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getsubDealerAddress() {
        return SubDealerAddress;
    }

    public void setsubDealerAddress(String subDealerAddress) {
        SubDealerAddress = subDealerAddress;
    }

    public String getSubDealerId() {
        return SubDealerId;
    }

    public void setSubDealerId(String subDealerId) {
        SubDealerId = subDealerId;
    }

    public String getSubDealerName() {
        return SubDealerName;
    }

    public void setSubDealerName(String subDealerName) {
        SubDealerName = subDealerName;
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