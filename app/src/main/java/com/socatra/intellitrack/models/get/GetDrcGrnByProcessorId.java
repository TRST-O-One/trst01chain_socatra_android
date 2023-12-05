package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDrcGrnByProcessorId {

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("DRCHdrId")
    @Expose
    private int DRCHdrId;

    @SerializedName("GRNId")
    @Expose
    private int GRNId;

    @SerializedName("DRCValue")
    @Expose
    private int DRCValue;

    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;

    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    @SerializedName("CreatedByUserId")
    @Expose
    private int CreatedByUserId;

    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;

    @SerializedName("UpdatedByUserId")
    @Expose
    private int UpdatedByUserId;

    @SerializedName("Quantity")
    @Expose
    private int Quantity;

    @SerializedName("GRNnumber")
    @Expose
    private String GRNnumber;

    @SerializedName("CurrentQty")
    @Expose
    private int CurrentQty;

    @SerializedName("GRNdate")
    @Expose
    private String GRNdate;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getDRCHdrId() {
        return DRCHdrId;
    }

    public void setDRCHdrId(int DRCHdrId) {
        this.DRCHdrId = DRCHdrId;
    }

    public int getGRNId() {
        return GRNId;
    }

    public void setGRNId(int GRNId) {
        this.GRNId = GRNId;
    }

    public int getDRCValue() {
        return DRCValue;
    }

    public void setDRCValue(int DRCValue) {
        this.DRCValue = DRCValue;
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(int CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public int getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(int UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String GRNnumber) {
        this.GRNnumber = GRNnumber;
    }

    public int getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(int CurrentQty) {
        this.CurrentQty = CurrentQty;
    }

    public String getGRNdate() {
        return GRNdate;
    }

    public void setGRNdate(String GRNdate) {
        this.GRNdate = GRNdate;
    }

    public String toString() {
        return GRNnumber;
    }
}

