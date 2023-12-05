package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQualityCheckDetails  {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("BatchCode")
    @Expose
    private String batchCode;
    @SerializedName("QualityCheckDate")
    @Expose
    private String qualityCheckDate;
    @SerializedName("EvidenceDocument")
    @Expose
    private String evidenceDocument;
    @SerializedName("QuantityRate")
    @Expose
    private Double quantityRate;
    @SerializedName("ProcessorId")
    @Expose
    private Integer processorId;
    @SerializedName("ProcessorName")
    @Expose
    private Object processorName;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getQualityCheckDate() {
        return qualityCheckDate;
    }

    public void setQualityCheckDate(String qualityCheckDate) {
        this.qualityCheckDate = qualityCheckDate;
    }

    public String getEvidenceDocument() {
        return evidenceDocument;
    }

    public void setEvidenceDocument(String evidenceDocument) {
        this.evidenceDocument = evidenceDocument;
    }

    public Double getQuantityRate() {
        return quantityRate;
    }

    public void setQuantityRate(Double quantityRate) {
        this.quantityRate = quantityRate;
    }

    public Integer getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Integer processorId) {
        this.processorId = processorId;
    }

    public Object getProcessorName() {
        return processorName;
    }

    public void setProcessorName(Object processorName) {
        this.processorName = processorName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}