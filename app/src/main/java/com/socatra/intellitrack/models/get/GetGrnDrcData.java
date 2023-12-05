package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGrnDrcData {

    @SerializedName("data")
    @Expose
    private List<GetDrcGrnByProcessorId> data;
    @SerializedName("messgae")
    @Expose
    private String messgae;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<GetDrcGrnByProcessorId> getData() {
        return data;
    }

    public void setData(List<GetDrcGrnByProcessorId> data) {
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
