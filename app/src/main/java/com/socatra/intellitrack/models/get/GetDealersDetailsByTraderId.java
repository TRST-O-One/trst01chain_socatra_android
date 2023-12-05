package com.socatra.intellitrack.models.get;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetDealersDetailsByTraderId {

    @SerializedName("DealerName")
    @Expose
    private String dealerName;

    @SerializedName("SubDealerCount")
    @Expose
    private Integer subDealerCount;

    @SerializedName("DealerId")
    @Expose
    private String dealerId;
    @SerializedName("FarmerCount")
    @Expose
    private Integer farmerCount;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getSubDealerCount() {
        return subDealerCount;
    }

    public void setSubDealerCount(Integer subDealerCount) {
        this.subDealerCount = subDealerCount;
    }

    public Integer getFarmerCount() {
        return farmerCount;
    }

    public void setFarmerCount(Integer farmerCount) {
        this.farmerCount = farmerCount;
    }

}