package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDealerProcurementDetails {


    @SerializedName("SumofTotalProcurement")
    @Expose
    private Integer SumofTotalProcurement;

    @SerializedName("NumberofTotalProcurement")
    @Expose
    private Integer NumberofTotalProcurement;




    public Integer getSumofTotalProcurement() {
        return SumofTotalProcurement;
    }

    public void setSumofTotalProcurement(Integer SumofTotalProcurement) {
        this.SumofTotalProcurement = SumofTotalProcurement;
    }


    public Integer getNumberofTotalProcurement() {
        return NumberofTotalProcurement;
    }

    public void setNumberofTotalProcurement(Integer NumberofTotalProcurement) {
        this.NumberofTotalProcurement = NumberofTotalProcurement;
    }
}
