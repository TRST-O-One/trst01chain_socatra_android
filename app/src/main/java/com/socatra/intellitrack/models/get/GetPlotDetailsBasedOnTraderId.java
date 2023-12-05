
        package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetPlotDetailsBasedOnTraderId {

    @SerializedName("FarmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("FarmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("TotalPlots")
    @Expose
    private Integer totalPlots;

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public Integer getTotalPlots() {
        return totalPlots;
    }

    public void setTotalPlots(Integer totalPlots) {
        this.totalPlots = totalPlots;
    }

}