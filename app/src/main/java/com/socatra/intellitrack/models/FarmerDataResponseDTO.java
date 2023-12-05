package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.socatra.intellitrack.database.entity.GetFarmerListFromServerTable;

import java.util.List;

public class FarmerDataResponseDTO {
    @SerializedName("data")
    @Expose
    private List<GetFarmerListFromServerTable> data;
    @SerializedName("messgae")
    @Expose
    private String messgae;
    @SerializedName("status")
    @Expose
    private Integer status;


    public List<GetFarmerListFromServerTable> getData() {
        return data;
    }

    public void setData(List<GetFarmerListFromServerTable> data) {
        this.data = data;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}



