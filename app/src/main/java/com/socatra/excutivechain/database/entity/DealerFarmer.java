package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class DealerFarmer {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    private int Id;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("DealerId")
    @Expose
    private int DealerId;

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

    public int getDealerId() {
        return DealerId;
    }

    public void setDealerId(int DealerId) {
        this.DealerId = DealerId;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean getSync() {
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
