package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetdashboardTotalProcurement {


    @SerializedName("SumofTotalLastYearProcurement")
    @Expose
    private Integer SumofTotalLastYearProcurement;

    @SerializedName("SumofTotalLastMonthProcurement")
    @Expose
    private Integer SumofTotalLastMonthProcurement;

    @SerializedName("SumofTotalLastWeekProcurement")
    @Expose
    private Integer SumofTotalLastWeekProcurement;





    public Integer getSumofTotalLastYearProcurement() {
        return SumofTotalLastYearProcurement;
    }

    public void setSumofTotalLastYearProcurement(Integer SumofTotalLastYearProcurement) {
        this.SumofTotalLastYearProcurement = SumofTotalLastYearProcurement;
    }


    public Integer getSumofTotalLastMonthProcurement() {
        return SumofTotalLastMonthProcurement;
    }

    public void setSumofTotalLastMonthProcurement(Integer SumofTotalLastMonthProcurement) {
        this.SumofTotalLastMonthProcurement = SumofTotalLastMonthProcurement;
    }



    public Integer getSumofTotalLastWeekProcurement() {
        return SumofTotalLastWeekProcurement;
    }

    public void setSumofTotalLastWeekProcurement(Integer SumofTotalLastWeekProcurement) {
        this.SumofTotalLastWeekProcurement = SumofTotalLastWeekProcurement;
    }




}



