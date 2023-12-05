package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPlotsDetailsByFarmercode {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("PlotCode")
    @Expose
    private String PlotCode;
    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;
    @SerializedName("TypeOfOwnership")
    @Expose
    private String TypeOfOwnership;
    @SerializedName("AreaInHectors")
    @Expose
    private Integer AreaInHectors;
    @SerializedName("GeoboundariesArea")
    @Expose
    private String GeoboundariesArea;
    @SerializedName("Latitude")
    @Expose
    private Double Latitude;
    @SerializedName("Longitude")
    @Expose
    private Double Longitude;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("VillageId")
    @Expose
    private Integer VillageId;
    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer CreatedByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private Integer UpdatedByUserId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

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

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        this.FarmerName = farmerName;
    }

    public String getTypeOfOwnership() {
        return TypeOfOwnership;
    }

    public void setTypeOfOwnership(String typeOfOwnership) {
        this.TypeOfOwnership = typeOfOwnership;
    }

    public Integer getAreaInHectors() {
        return AreaInHectors;
    }

    public void setAreaInHectors(Integer areaInHectors) {
        this.AreaInHectors = areaInHectors;
    }

    public String getGeoboundariesArea() {
        return GeoboundariesArea;
    }

    public void setGeoboundariesArea(String geoboundariesArea) {
        this.GeoboundariesArea = geoboundariesArea;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public Integer getVillageId() {
        return VillageId;
    }

    public void setVillageId(Integer villageId) {
        this.VillageId = villageId;
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        this.IsActive = isActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.UpdatedDate = updatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.UpdatedByUserId = updatedByUserId;
    }

}