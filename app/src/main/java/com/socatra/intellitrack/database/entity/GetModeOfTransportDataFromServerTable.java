package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetModeOfTransportDataFromServerTable {
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

    @SerializedName("VehicleRegistrationNumber")
    @Expose
    private String VehicleRegistrationNumber;

    @SerializedName("InsuranceExpiryDate")
    @Expose
    private String InsuranceExpiryDate;

    @SerializedName("ManufacturingYear")
    @Expose
    private String ManufacturingYear;

    @SerializedName("VehicleType")
    @Expose
    private String VehicleType;



    @SerializedName("LastMaintenanceDate")
    @Expose
    private String LastMaintenanceDate;


    @SerializedName("VehicleLoadCapacity")
    @Expose
    private String VehicleLoadCapacity;


    @SerializedName("Fueltype")
    @Expose
    private String Fueltype;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleRegistrationNumber() {
        return VehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        VehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getInsuranceExpiryDate() {
        return InsuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(String insuranceExpiryDate) {
        InsuranceExpiryDate = insuranceExpiryDate;
    }

    public String getManufacturingYear() {
        return ManufacturingYear;
    }

    public void setManufacturingYear(String manufacturingYear) {
        ManufacturingYear = manufacturingYear;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getLastMaintenanceDate() {
        return LastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        LastMaintenanceDate = lastMaintenanceDate;
    }

    public String getVehicleLoadCapacity() {
        return VehicleLoadCapacity;
    }

    public void setVehicleLoadCapacity(String vehicleLoadCapacity) {
        VehicleLoadCapacity = vehicleLoadCapacity;
    }

    public String getFueltype() {
        return Fueltype;
    }

    public void setFueltype(String fueltype) {
        Fueltype = fueltype;
    }

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