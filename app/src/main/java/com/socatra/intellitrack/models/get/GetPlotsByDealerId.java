package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPlotsByDealerId {

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("FarmerId")
    @Expose
    private Integer FarmerId;
    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;
    @SerializedName("TotalPlots")
    @Expose
    private Integer TotalPlots;

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.FarmerCode = farmerCode;
    }

    public Integer getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.FarmerId = farmerId;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        this.FarmerName = farmerName;
    }

    public Integer getTotalPlots() {
        return TotalPlots;
    }

    public void setTotalPlots(Integer totalPlots) {
        this.TotalPlots = totalPlots;
    }

}
