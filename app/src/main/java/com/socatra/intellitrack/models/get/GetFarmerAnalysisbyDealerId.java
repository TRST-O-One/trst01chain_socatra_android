package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFarmerAnalysisbyDealerId {


    @SerializedName("NoOfPlantations")
    @Expose
    private Integer NoOfPlantations;

    @SerializedName("NoOfFarmers")
    @Expose
    private Integer NoOfFarmers;

    @SerializedName("TotalArea")
    @Expose
    private Double TotalArea;

    @SerializedName("TotalSupplyInTons")
    @Expose
    private Double TotalSupplyInTons;


    @SerializedName("NoofSubDealers")
    @Expose
    private Integer NoofSubDealers;

    @SerializedName("TotalProcurement")
    @Expose
    private Double TotalProcurement;


    @SerializedName("MainDealer")
    @Expose
    private Integer MainDealer;

    @SerializedName("TotalInvoiceSupply")
    @Expose
    private Double TotalInvoiceSupply;

    @SerializedName("ProcessorCount")
    @Expose
    private Integer ProcessorCount;




    public Integer getNoOfPlantations() {
        return NoOfPlantations;
    }

    public void setNoOfPlantations(Integer noOfPlantations) {
        this.NoOfPlantations = noOfPlantations;
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

    public Integer getNoofSubDealers() {
        return NoofSubDealers;
    }

    public void setNoofSubDealers(Integer NoofSubDealers) {
        this.NoofSubDealers = NoofSubDealers;
    }

    public Double getTotalProcurement() {
        return TotalProcurement;
    }

    public void setTotalProcurement(Double TotalProcurement) {
        this.TotalProcurement = TotalProcurement;
    }

    public Integer getMainDealer() {
        return MainDealer;
    }

    public void setMainDealer(Integer MainDealer) {
        this.MainDealer = MainDealer;
    }

    public Double getTotalInvoiceSupply() {
        return TotalInvoiceSupply;
    }

    public void setTotalInvoiceSupply(Double TotalInvoiceSupply) {
        this.TotalInvoiceSupply = TotalInvoiceSupply;
    }

    public Integer getProcessorCount() {
        return ProcessorCount;
    }

    public void setProcessorCount(Integer ProcessorCount) {
        this.ProcessorCount = ProcessorCount;
    }



}


