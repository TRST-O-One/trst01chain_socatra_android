package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class PlantationDocuments {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    private int ID;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("PlotCode")
    @Expose
    private String PlotCode;

    @SerializedName("DocUrlValue")
    @Expose
    private String DocUrlValue;

    @SerializedName("DocURL")
    @Expose
    private String DocURL;
    @SerializedName("DocType")
    @Expose
    private String DocType;
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

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String FarmerCode) {
        this.FarmerCode = FarmerCode;
    }

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String PlotCode) {
        this.PlotCode = PlotCode;
    }

    public String getDocUrlValue() {
        return DocUrlValue;
    }

    public void setDocUrlValue(String DocUrlValue) {
        this.DocUrlValue = DocUrlValue;
    }

    public String getDocURL() {
        return DocURL;
    }

    public void setDocURL(String DocURL) {
        this.DocURL = DocURL;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String DocType) {
        this.DocType = DocType;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
}
