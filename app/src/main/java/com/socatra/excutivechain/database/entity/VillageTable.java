package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class VillageTable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("villageId")
    @Expose(serialize = false, deserialize = false)
    private int villageId;

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("Code")
    @Expose
    private String Code = "";


    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("SubDistrictId")
    @Expose
    private String SubDistrictId;



    @SerializedName("PinCode")
    @Expose
    private String PinCode;


    @SerializedName("IsActive")
    @Expose
    private String IsActive = "1";

    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    @SerializedName("CreatedByUserId")
    @Expose
    private String CreatedByUserId ;

    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;

    @SerializedName("UpdatedByUserId")
    @Expose
    private String UpdatedByUserId ;


    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }

    public String getId() {
        return Id;
    }


    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubDistrictId() {
        return SubDistrictId;
    }

    public void setSubDistrictId(String SubDistrictId) {
        this.SubDistrictId = SubDistrictId;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    @Override
    public String toString() {
        return Name;
    }
}