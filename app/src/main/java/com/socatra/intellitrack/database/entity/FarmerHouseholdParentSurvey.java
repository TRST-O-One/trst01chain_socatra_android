package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class FarmerHouseholdParentSurvey {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose (serialize = false, deserialize = false)
    private int Id;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("FarmerId")
    @Expose
    private String FarmerId;
    @SerializedName("FamilyCount")
    @Expose
    private int FamilyCount;
    @SerializedName("MaritalStatus")
    @Expose
    private String MaritalStatus;
    @SerializedName("SpouseName")
    @Expose
    private String SpouseName;
    @SerializedName("Age")
    @Expose
    private int Age;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("Occupation")
    @Expose
    private String Occupation;
    @SerializedName("NoofChildren")
    @Expose
    private Integer NoofChildren;
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


    public String getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(String FarmerId) {
        this.FarmerId = FarmerId;
    }

    public int getFamilyCount() {
        return FamilyCount;
    }

    public void setFamilyCount(int FamilyCount) {
        this.FamilyCount = FamilyCount;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String MaritalStatus) {
        this.MaritalStatus = MaritalStatus;
    }

    public String getSpouseName() {
        return SpouseName;
    }

    public void setSpouseName(String SpouseName) {
        this.SpouseName = SpouseName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public Integer getNoofChildren() {
        return NoofChildren;
    }

    public void setNoofChildren(Integer NoofChildren) {
        this.NoofChildren = NoofChildren;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "FarmerHouseholdParentSurvey{" +
                "Id=" + Id +
                ", FarmerCode='" + FarmerCode + '\'' +
                ", FarmerId='" + FarmerId + '\'' +
                ", FamilyCount=" + FamilyCount +
                ", MaritalStatus='" + MaritalStatus + '\'' +
                ", SpouseName='" + SpouseName + '\'' +
                ", Age=" + Age +
                ", Gender='" + Gender + '\'' +
                ", Occupation='" + Occupation + '\'' +
                ", NoofChildren=" + NoofChildren +
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
