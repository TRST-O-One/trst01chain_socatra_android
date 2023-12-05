package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetdashboardTotalSupply{


    @SerializedName("SumofTotalLastYearSupply")
    @Expose
    private Integer SumofTotalLastYearSupply;

    @SerializedName("SumofTotalLastMonthSupply")
    @Expose
    private Integer SumofTotalLastMonthSupply;

    @SerializedName("SumofTotalLastWeekSupply")
    @Expose
    private Integer SumofTotalLastWeekSupply;





    public Integer getSumofTotalLastYearSupply() {
        return SumofTotalLastYearSupply;
    }

    public void setSumofTotalLastYearSupply(Integer SumofTotalLastYearSupply) {
        this.SumofTotalLastYearSupply = SumofTotalLastYearSupply;
    }


    public Integer getSumofTotalLastMonthSupply() {
        return SumofTotalLastMonthSupply;
    }

    public void setSumofTotalLastMonthSupply(Integer SumofTotalLastMonthSupply) {
        this.SumofTotalLastMonthSupply = SumofTotalLastMonthSupply;
    }



    public Integer getSumofTotalLastWeekSupply() {
        return SumofTotalLastWeekSupply;
    }

    public void setSumofTotalLastWeekSupply(Integer SumofTotalLastWeekSupply) {
        this.SumofTotalLastWeekSupply = SumofTotalLastWeekSupply;
    }




}




