package com.socatra.intellitrack.models.get;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetDashboardDataByTraderId {

    @SerializedName("NoOfFarmers")
    @Expose
    private Integer noOfFarmers;
    @SerializedName("NoOfPlantations")
    @Expose
    private Integer noOfPlantations;
    @SerializedName("TotalArea")
    @Expose
    private Double totalArea;
    @SerializedName("NoOfDealers")
    @Expose
    private Integer noOfDealers;
    @SerializedName("NoOfProcessors")
    @Expose
    private Integer noOfProcessors;

    public Integer getNoOfFarmers() {
        return noOfFarmers;
    }

    public void setNoOfFarmers(Integer noOfFarmers) {
        this.noOfFarmers = noOfFarmers;
    }

    public Integer getNoOfPlantations() {
        return noOfPlantations;
    }

    public void setNoOfPlantations(Integer noOfPlantations) {
        this.noOfPlantations = noOfPlantations;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getNoOfDealers() {
        return noOfDealers;
    }

    public void setNoOfDealers(Integer noOfDealers) {
        this.noOfDealers = noOfDealers;
    }

    public Integer getNoOfProcessors() {
        return noOfProcessors;
    }

    public void setNoOfProcessors(Integer noOfProcessors) {
        this.noOfProcessors = noOfProcessors;
    }

}
