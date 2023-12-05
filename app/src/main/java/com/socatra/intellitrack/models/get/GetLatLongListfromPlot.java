package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLatLongListfromPlot {

    @SerializedName("PlotCode")
    @Expose
    private String PlotCode;
    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("LatLong")
    @Expose
    private String LatLong;
    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;
    @SerializedName("PlotAddress")
    @Expose
    private String PlotAddress;
    @SerializedName("AreaInHectors")
    @Expose
    private Double AreaInHectors;
    @SerializedName("Latitude")
    @Expose
    private Double Latitude;
    @SerializedName("Longitude")
    @Expose
    private Double Longitude;
    @SerializedName("GeoboundariesArea")
    @Expose
    private String GeoboundariesArea;

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String plotCode) {
        this.PlotCode = plotCode;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.FarmerCode = farmerCode;
    }

    public String getLatLong() {
        return LatLong;
    }

    public void setLatLong(String latLong) {
        this.LatLong = latLong;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        this.FarmerName = farmerName;
    }

    public String getPlotAddress() {
        return PlotAddress;
    }

    public void setPlotAddress(String plotAddress) {
        this.PlotAddress = plotAddress;
    }

    public Double getAreaInHectors() {
        return AreaInHectors;
    }

    public void setAreaInHectors(Double areaInHectors) {
        this.AreaInHectors = areaInHectors;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        this.Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        this.Longitude = longitude;
    }

    public String getGeoboundariesArea() {
        return GeoboundariesArea;
    }

    public void setGeoboundariesArea(String geoboundariesArea) {
        this.GeoboundariesArea = geoboundariesArea;
    }

}