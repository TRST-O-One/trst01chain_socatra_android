package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity
public class FarmersTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("FarmerId")
    @Expose (serialize = false, deserialize = false)
    private int FarmerId;

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("FirstName")
    @Expose
    private String FirstName;
    @SerializedName("LastName")
    @Expose
    private String LastName;
    @SerializedName("FatherName")
    @Expose
    private String FatherName;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("Age")
    @Expose
    private String Age;
    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("VillageId")
    @Expose
    private String VillageId;
    @SerializedName("NationalIdentityCode")
    @Expose
    private String NationalIdentityCode;
    @SerializedName("NationalIdentityCodeDocument")
    @Expose
    private String NationalIdentityCodeDocument;
    @SerializedName("NoOfPlots")
    @Expose
    private String NoOfPlots;
    @SerializedName("Image")
    @Expose
    private String Image;
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


    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String FarmerCode) {
        this.FarmerCode = FarmerCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
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

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String VillageId) {
        this.VillageId = VillageId;
    }

    public String getNationalIdentityCode() {
        return NationalIdentityCode;
    }

    public void setNationalIdentityCode(String NationalIdentityCode) {
        this.NationalIdentityCode = NationalIdentityCode;
    }

    public String getNationalIdentityCodeDocument() {
        return NationalIdentityCodeDocument;
    }

    public void setNationalIdentityCodeDocument(String NationalIdentityCodeDocument) {
        this.NationalIdentityCodeDocument = NationalIdentityCodeDocument;
    }

    public String getNoOfPlots() {
        return NoOfPlots;
    }

    public void setNoOfPlots(String NoOfPlots) {
        this.NoOfPlots = NoOfPlots;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
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

    public int getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(int farmerId) {
        FarmerId = farmerId;
    }

    public String getServerSync() {
        return ServerSync;
    }

    public void setServerSync(String serverSync) {
        ServerSync = serverSync;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return "FarmersTable{" +
                "FarmerId=" + FarmerId +
                ", FarmerCode='" + FarmerCode + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", FatherName='" + FatherName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Age='" + Age + '\'' +
                ", PrimaryContactNo='" + PrimaryContactNo + '\'' +
                ", Address='" + Address + '\'' +
                ", VillageId='" + VillageId + '\'' +
                ", NationalIdentityCode='" + NationalIdentityCode + '\'' +
                ", NationalIdentityCodeDocument='" + NationalIdentityCodeDocument + '\'' +
                ", NoOfPlots='" + NoOfPlots + '\'' +
                ", Image='" + Image + '\'' +
                ", IsActive='" + IsActive + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", CreatedByUserId='" + CreatedByUserId + '\'' +
                ", UpdatedDate='" + UpdatedDate + '\'' +
                ", UpdatedByUserId='" + UpdatedByUserId + '\'' +
                ", sync=" + sync +
                ", ServerSync='" + ServerSync + '\'' +
                '}';
    }
}
