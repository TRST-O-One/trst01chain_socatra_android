package com.socatra.intellitrack.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDRCDtlDetailsDTO {

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
    private String IsActive;

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

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String IsActive) {
        this.IsActive = IsActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(String CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(String UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    @Override
    public String toString() {
        return "PostDRCDtlDetailsDTO{" +
                "Id=" + Id +
                ", DRCHdrId=" + DRCHdrId +
                ", GRNId=" + GRNId +
                ", DRCValue=" + DRCValue +
                ", IsActive='" + IsActive + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", CreatedByUserId='" + CreatedByUserId + '\'' +
                ", UpdatedDate='" + UpdatedDate + '\'' +
                ", UpdatedByUserId='" + UpdatedByUserId + '\'' +
                '}';
    }
}

