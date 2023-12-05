package com.socatra.excutivechain.database.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Plantation {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("PlotId")
    @Expose(serialize = false, deserialize = false)
    private int PlotId;

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("PlotCode")
    @Expose
    private String PlotCode;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("TypeOfOwnership")
    @Expose
    private String TypeOfOwnership;
    @SerializedName("AreaInHectors")
    @Expose
    private Double AreaInHectors;

    @SerializedName("GeoboundariesArea")
    @Expose
    private Double GeoboundariesArea;
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
    private String VillageId;

    @SerializedName("LabourStatus")
    @Expose
    private String LabourStatus;

    @SerializedName("IsActive")
    @Expose
    private String IsActive;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private String CreatedByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private String UpdatedByUserId;

    @SerializedName("sync")
    @Expose
    private boolean sync;

    @SerializedName("ServerSync")
    @Expose
    private String ServerSync;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String PlotCode) {
        this.PlotCode = PlotCode;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String FarmerCode) {
        this.FarmerCode = FarmerCode;
    }

    public String getTypeOfOwnership() {
        return TypeOfOwnership;
    }

    public void setTypeOfOwnership(String TypeOfOwnership) {
        this.TypeOfOwnership = TypeOfOwnership;
    }

    public Double getAreaInHectors() {
        return AreaInHectors;
    }

    public void setAreaInHectors(Double AreaInHectors) {
        this.AreaInHectors = AreaInHectors;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String VillageId) {
        this.VillageId = VillageId;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String IsActive) {
        this.IsActive = IsActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(String CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(String UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    public int getPlotId() {
        return PlotId;
    }

    public void setPlotId(int PlotId) {
        this.PlotId = PlotId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getServerSync() {
        return ServerSync;
    }

    public void setServerSync(String serverSync) {
        ServerSync = serverSync;
    }

    public Double getGeoboundariesArea() {
        return GeoboundariesArea;
    }

    public void setGeoboundariesArea(Double GeoboundariesArea) {
        this.GeoboundariesArea = GeoboundariesArea;
    }

    public String getLabourStatus() {
        return LabourStatus;
    }

    public void setLabourStatus(String LabourStatus) {
        this.LabourStatus = LabourStatus;
    }

    @Override
    public String toString() {
        return PlotCode;
    }
}
