package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetGrnDetailsByDealerIDFromServer {
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

    @SerializedName("GRNnumber")
    @Expose
    private String GRNnumber;


    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;

    @SerializedName("farmeradress")
    @Expose
    private String farmeradress;

    @SerializedName("ManfacturerId")
    @Expose
    private String ManfacturerId;

    @SerializedName("Dealer")
    @Expose
    private String Dealer;

    @SerializedName("DealerId")
    @Expose
    private String DealerId;

    @SerializedName("FarmerId")
    @Expose
    private String FarmerId;

    @SerializedName("Quantity")
    @Expose
    private String Quantity;

    @SerializedName("CurrentQty")
    @Expose
    private String CurrentQty;


    @SerializedName("GRNdate")
    @Expose
    private String GRNdate;

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

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String GRNnumber) {
        this.GRNnumber = GRNnumber;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        FarmerCode = farmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getFarmeradress() {
        return farmeradress;
    }

    public void setFarmeradress(String farmeradress) {
        this.farmeradress = farmeradress;
    }

    public String getManfacturerId() {
        return ManfacturerId;
    }

    public void setManfacturerId(String manfacturerId) {
        ManfacturerId = manfacturerId;
    }

    public String getDealer() {
        return Dealer;
    }

    public void setDealer(String dealer) {
        Dealer = dealer;
    }

    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        DealerId = dealerId;
    }

    public String getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(String farmerId) {
        FarmerId = farmerId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(String currentQty) {
        CurrentQty = currentQty;
    }

    public String getGRNdate() {
        return GRNdate;
    }

    public void setGRNdate(String GRNdate) {
        this.GRNdate = GRNdate;
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