package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetCustomerDataFromServer {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    private int id_new;

    public int getId_new() {
        return id_new;
    }

    public void setId_new(int id_new) {
        this.id_new = id_new;
    }

    @SerializedName("Id")
    @Expose
    private String id;


    @SerializedName("EntityName")
    @Expose
    private String EntityName;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Phone")
    @Expose
    private String Phone;

    @SerializedName("VillageName")
    @Expose
    private String VillageName;

    @SerializedName("SubDistrictname")
    @Expose
    private String SubDistrictname;

    @SerializedName("DistrictorRegencyName")
    @Expose
    private String DistrictorRegencyName;

    @SerializedName("StateorProvinceName")
    @Expose
    private String StateorProvinceName;

    @SerializedName("CountryName")
    @Expose
    private String CountryName;


    @SerializedName("IsActive")
    @Expose
    private String IsActive = "true";

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String entityName) {
        EntityName = entityName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getSubDistrictname() {
        return SubDistrictname;
    }

    public void setSubDistrictname(String subDistrictname) {
        SubDistrictname = subDistrictname;
    }

    public String getDistrictorRegencyName() {
        return DistrictorRegencyName;
    }

    public void setDistrictorRegencyName(String districtorRegencyName) {DistrictorRegencyName = districtorRegencyName;}

    public String getStateorProvinceName() {
        return StateorProvinceName;
    }

    public void setStateorProvinceName(String stateorProvinceName) {StateorProvinceName = stateorProvinceName;}

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {CountryName = countryName;}



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
}

