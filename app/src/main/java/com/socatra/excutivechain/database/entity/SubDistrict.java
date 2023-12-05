package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class SubDistrict {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("SubDistrictId")
    @Expose(serialize = false, deserialize = false)
    private int SubDistrictId;

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("Code")
    @Expose
    private String Code;
    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("DistrictId")
    @Expose
    private Integer DistrictId;
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

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Integer getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(Integer DistrictId) {
        this.DistrictId = DistrictId;
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

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    public int getSubDistrictId() {
        return SubDistrictId;
    }

    public void setSubDistrictId(int SubDistrictId) {
        this.SubDistrictId = SubDistrictId;
    }

    @Override
    public String toString() {
        return Name;
    }
}
