package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLatLongListfromBatchId {

    @SerializedName("GRNnumber")
    @Expose
    private String gRNnumber;
    @SerializedName("PlotCode")
    @Expose
    private String plotCode;
    @SerializedName("FarmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("GeoboundariesArea")
    @Expose
    private String geoboundariesArea;
    @SerializedName("AreaInHectors")
    @Expose
    private Double areaInHectors;
    @SerializedName("LatLong")
    @Expose
    private String latLong;

    public String getGRNnumber() {
        return gRNnumber;
    }

    public void setGRNnumber(String gRNnumber) {
        this.gRNnumber = gRNnumber;
    }

    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String plotCode) {
        this.plotCode = plotCode;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getGeoboundariesArea() {
        return geoboundariesArea;
    }

    public void setGeoboundariesArea(String geoboundariesArea) {
        this.geoboundariesArea = geoboundariesArea;
    }

    public Double getAreaInHectors() {
        return areaInHectors;
    }

    public void setAreaInHectors(Double areaInHectors) {
        this.areaInHectors = areaInHectors;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

}