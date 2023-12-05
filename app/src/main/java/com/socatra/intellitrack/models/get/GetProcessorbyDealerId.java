package com.socatra.intellitrack.models.get;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProcessorbyDealerId {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("DealerId")
    @Expose
    private Integer dealerId;
    @SerializedName("ProcessorId")
    @Expose
    private Integer processorId;
    @SerializedName("DealerName")
    @Expose
    private String dealerName;
    @SerializedName("Processor")
    @Expose
    private String processor;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("PrimaryContactNo")
    @Expose
    private String primaryContactNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Integer processorId) {
        this.processorId = processorId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }
}
