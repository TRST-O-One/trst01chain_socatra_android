package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class DealerMaster {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("DealerId")
    @Expose(serialize = false, deserialize = false)
    private int DealerId;

    @SerializedName("Id")
    @Expose
    private int Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("Village")
    @Expose
    private int Village;
    @SerializedName("Latitude")
    @Expose
    private Double Latitude;
    @SerializedName("Longitute")
    @Expose
    private Double Longitute;
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
    @SerializedName("ManufacturerId")
    @Expose
    private int ManufacturerId;
    @SerializedName("IsSubDealer")
    @Expose
    private String IsSubDealer;
    @SerializedName("IsMapping")
    @Expose
    private String IsMapping;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String PrimaryContactNo) {
        this.PrimaryContactNo = PrimaryContactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public int getVillage() {
        return Village;
    }

    public void setVillage(int Village) {
        this.Village = Village;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    public Double getLongitute() {
        return Longitute;
    }

    public void setLongitute(Double Longitute) {
        this.Longitute = Longitute;
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

    public void setManufacturerId(int ManufacturerId) {
        this.ManufacturerId = ManufacturerId;
    }

    public String getIsSubDealer() {
        return IsSubDealer;
    }

    public void setIsSubDealer(String IsSubDealer) {
        this.IsSubDealer = IsSubDealer;
    }

    public String getIsMapping() {
        return IsMapping;
    }

    public void setIsMapping(String IsMapping) {
        this.IsMapping = IsMapping;
    }

    public int getDealerId() {
        return DealerId;
    }

    public void setDealerId(int dealerId) {
        DealerId = dealerId;
    }

    @Override
    public String toString() {
        return Name;
    }
}
