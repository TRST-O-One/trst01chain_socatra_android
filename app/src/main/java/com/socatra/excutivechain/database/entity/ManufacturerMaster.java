package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class ManufacturerMaster {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("ManufacturerId")
    @Expose(serialize = false, deserialize = false)
    private int ManufacturerId;

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("EntityName")
    @Expose
    private String EntityName;

    @SerializedName("PrimaryContactName")
    @Expose
    private String PrimaryContactName;

    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;

    @SerializedName("SecondaryContactNo")
    @Expose
    private String SecondaryContactNo;

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
    private int VillageId;

    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private int CreatedByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private int UpdatedByUserId;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String EntityName) {
        this.EntityName = EntityName;
    }

    public String getPrimaryContactName() {
        return PrimaryContactName;
    }

    public void setPrimaryContactName(String PrimaryContactName) {
        this.PrimaryContactName = PrimaryContactName;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String PrimaryContactNo) {
        this.PrimaryContactNo = PrimaryContactNo;
    }

    public String getSecondaryContactNo() {
        return SecondaryContactNo;
    }

    public void setSecondaryContactNo(String SecondaryContactNo) {
        this.SecondaryContactNo = SecondaryContactNo;
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

    public int getVillageId() {
        return VillageId;
    }

    public void setVillageId(int VillageId) {
        this.VillageId = VillageId;
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(int CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public int getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(int UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    public int getManufacturerId() {
        return ManufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        ManufacturerId = manufacturerId;
    }

    @Override
    public String toString() {
        return EntityName;
    }
}
