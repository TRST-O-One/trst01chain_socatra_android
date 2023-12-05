package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetDrcdetailsFromServer {
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

    @SerializedName("DRCHdrId")
    @Expose
    private String DRCHdrId;

    @SerializedName("GRNId")
    @Expose
    private String GRNId;

    @SerializedName("DRCValue")
    @Expose
    private String DRCValue;

    @SerializedName("Quantity")
    @Expose
    private String Quantity;



    @SerializedName("GRNnumber")
    @Expose
    private String GRNnumber;

    @SerializedName("CurrentQty")
    @Expose
    private String CurrentQty;

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

    public String getDRCHdrId() {
        return DRCHdrId;
    }

    public void setDRCHdrId(String drcHdrId) {
        DRCHdrId = drcHdrId;
    }

    public String getGRNId() {
        return GRNId;
    }

    public void setGRNId(String grnId) {
        GRNId = grnId;
    }

    public String getDRCValue() {
        return DRCValue;
    }

    public void setDRCValue(String drcValue) {
        DRCValue = drcValue;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String grnnumber) {
        GRNnumber = grnnumber;
    }

    public String getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(String currentQty) {
        CurrentQty = currentQty;
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