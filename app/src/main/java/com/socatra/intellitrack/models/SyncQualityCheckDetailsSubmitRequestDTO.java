package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SyncQualityCheckDetailsSubmitRequestDTO {

    @SerializedName("Id")
    @Expose
    private Object id;
    @SerializedName("BatchCode")
    @Expose
    private String batchCode;
    @SerializedName("QuantityRate")
    @Expose
    private String quantityRate;
    @SerializedName("EvidenceDocument")
    @Expose
    private String evidenceDocument;
    @SerializedName("QualityCheckDate")
    @Expose
    private String qualityCheckDate;
    @SerializedName("ProcessorId")
    @Expose
    private String processorId;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private String createdByUserId;
    @SerializedName("UpdatedByUserId")
    @Expose
    private String updatedByUserId;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getQuantityRate() {
        return quantityRate;
    }

    public void setQuantityRate(String quantityRate) {
        this.quantityRate = quantityRate;
    }

    public String getEvidenceDocument() {
        return evidenceDocument;
    }

    public void setEvidenceDocument(String evidenceDocument) {
        this.evidenceDocument = evidenceDocument;
    }

    public String getQualityCheckDate() {
        return qualityCheckDate;
    }

    public void setQualityCheckDate(String qualityCheckDate) {
        this.qualityCheckDate = qualityCheckDate;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }
}