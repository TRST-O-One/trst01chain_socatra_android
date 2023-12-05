package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFarmerAnalysisbyProcessorId {





    @SerializedName("NoOfPlantations")
    @Expose
    private Integer NoOfPlantations;

    @SerializedName("NoofDealers")
    @Expose
    private Integer NoofDealers;


    @SerializedName("NoOfFarmers")
    @Expose
    private Integer NoOfFarmers;

    @SerializedName("TotalArea")
    @Expose
    private Double TotalArea;

    @SerializedName("TotalSupplyInTons")
    @Expose
    private Double TotalSupplyInTons;


    @SerializedName("TotalInvoiceSupply")
    @Expose
    private Double TotalInvoiceSupply;


    @SerializedName("TotalProcurement")
    @Expose
    private Integer TotalProcurement;


    @SerializedName("CustomerCount")
    @Expose
    private Integer CustomerCount;








    public Integer getNoOfPlantations() {
        return NoOfPlantations;
    }

    public void setNoOfPlantations(Integer noOfPlantations) {
        this.NoOfPlantations = noOfPlantations;
    }


    public Integer getNoofDealers() {
        return NoofDealers;
    }

    public void setNoofDealers(Integer noofDealers) {
        this.NoofDealers = noofDealers;
    }

    public Integer getTotalProcurement() {
        return TotalProcurement;
    }

    public void setTotalProcurement(Integer TotalProcurement) {
        this.TotalProcurement = TotalProcurement;
    }


    public Integer getNoOfFarmers() {
        return NoOfFarmers;
    }

    public void setNoOfFarmers(Integer noOfFarmers) {
        this.NoOfFarmers = noOfFarmers;
    }



    public Double getTotalArea() {
        return TotalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.TotalArea = totalArea;
    }


    public Double getTotalSupplyInTons() {
        return TotalSupplyInTons;
    }

    public void setTotalSupplyInTons(Double totalSupplyInTons) {
        this.TotalSupplyInTons = totalSupplyInTons;
    }

    public Double getTotalInvoiceSupply() {
        return TotalInvoiceSupply;
    }

    public void setTotalInvoiceSupply(Double TotalInvoiceSupply) {
        this.TotalInvoiceSupply = TotalInvoiceSupply;
    }


    public Integer getCustomerCount() {
        return CustomerCount;
    }

    public void setCustomerCount(Integer CustomerCount) {
        this.CustomerCount = CustomerCount;
    }






}


