package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetFarmerDetailsByInvoiceId {

    @SerializedName("FarmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FarmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("Survey")
    @Expose
    private Integer survey;

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public Integer getSurvey() {
        return survey;
    }

    public void setSurvey(Integer survey) {
        this.survey = survey;
    }

}
