package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGrnProcessorData {

    @SerializedName("data")
    @Expose
    private List<GetGrnByProcessorId> data;
    @SerializedName("messgae")
    @Expose
    private String messgae;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<GetGrnByProcessorId> getData() {
        return data;
    }

    public void setData(List<GetGrnByProcessorId> data) {
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

    @Override
    public String toString() {
        return "GetGrnProcessorData{" +
                "data=" + data +
                ", messgae='" + messgae + '\'' +
                ", status=" + status +
                '}';
    }
}
