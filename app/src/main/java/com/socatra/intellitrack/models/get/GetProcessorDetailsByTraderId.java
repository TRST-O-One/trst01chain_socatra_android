
        package com.socatra.intellitrack.models.get;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetProcessorDetailsByTraderId {

    @SerializedName("ProcessorName")
    @Expose
    private String processorName;
    @SerializedName("ProcessorId")
    @Expose
    private Integer processorId;

    @SerializedName("FarmerCount")
    @Expose
    private Integer farmerCount;

    @SerializedName("DealerCount")
    @Expose
    private Integer dealerCount;

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public Integer getFarmerCount() {
        return farmerCount;
    }

    public void setFarmerCount(Integer farmerCount) {
        this.farmerCount = farmerCount;
    }

    public Integer getDealerCount() {
        return dealerCount;
    }

    public void setDealerCount(Integer dealerCount) {
        this.dealerCount = dealerCount;
    }

    public Integer getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Integer processorId) {
        this.processorId = processorId;
    }

}
