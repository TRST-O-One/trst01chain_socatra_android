package com.socatra.intellitrack.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDRCHdrDetailsDTO {

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("DRCNo")
    @Expose
    private String DRCNo;

    @SerializedName("DRCDocument")
    @Expose
    private String DRCDocument;
    @SerializedName("ProcessorId")
    @Expose
    private String ProcessorId;


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

    public String getDRCNo() {
        return DRCNo;
    }

    public void setDRCNo(String DRCNo) {
        this.DRCNo = DRCNo;
    }

    public String getDRCDocument() {
        return DRCDocument;
    }

    public void setDRCDocument(String DRCDocument) {
        this.DRCDocument = DRCDocument;
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

    public void setUpdatedByUserId(String  UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }


    public String getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(String processorId) {
        ProcessorId = processorId;
    }

    @Override
    public String toString() {
        return "PostDRCHdrDetailsDTO{" +
                "Id=" + Id +
                ", DRCNo='" + DRCNo + '\'' +
                ", DRCDocument='" + DRCDocument + '\'' +
                ", ProcessorId='" + DRCDocument + '\'' +
                ", IsActive='" + IsActive + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", CreatedByUserId='" + CreatedByUserId + '\'' +
                ", UpdatedDate='" + UpdatedDate + '\'' +
                ", UpdatedByUserId='" + UpdatedByUserId + '\'' +
                '}';
    }
}

