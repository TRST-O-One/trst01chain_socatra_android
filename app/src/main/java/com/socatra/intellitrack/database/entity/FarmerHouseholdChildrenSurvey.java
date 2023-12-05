package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class FarmerHouseholdChildrenSurvey {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    private int Id;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("FarmerHouseholdSurveyId")
    @Expose
    private int FarmerHouseholdSurveyId;
    @SerializedName("FarmerId")
    @Expose
    private String FarmerId;
    @SerializedName("ChildrenName")
    @Expose
    private String ChildrenName;
    @SerializedName("ChildrenGender")
    @Expose
    private String ChildrenGender;
    @SerializedName("ChildrenAge")
    @Expose
    private int ChildrenAge;
    @SerializedName("ChildrenOccupation")
    @Expose
    private String ChildrenOccupation;

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

    public int getFarmerHouseholdSurveyId() {
        return FarmerHouseholdSurveyId;
    }

    public void setFarmerHouseholdSurveyId(int FarmerHouseholdSurveyId) {
        this.FarmerHouseholdSurveyId = FarmerHouseholdSurveyId;
    }

    public String getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(String FarmerId) {
        this.FarmerId = FarmerId;
    }

    public String getChildrenName() {
        return ChildrenName;
    }

    public void setChildrenName(String ChildrenName) {
        this.ChildrenName = ChildrenName;
    }

    public String getChildrenGender() {
        return ChildrenGender;
    }

    public void setChildrenGender(String ChildrenGender) {
        this.ChildrenGender = ChildrenGender;
    }

    public Integer getChildrenAge() {
        return ChildrenAge;
    }

    public void setChildrenAge(int ChildrenAge) {
        this.ChildrenAge = ChildrenAge;
    }

    public String getChildrenOccupation() {
        return ChildrenOccupation;
    }

    public void setChildrenOccupation(String ChildrenOccupation) {
        this.ChildrenOccupation = ChildrenOccupation;
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
        return "FarmerHouseholdChildrenSurvey{" +
                "Id=" + Id +
                ", FarmerCode='" + FarmerCode + '\'' +
                ", FarmerHouseholdSurveyId=" + FarmerHouseholdSurveyId +
                ", FarmerId='" + FarmerId + '\'' +
                ", ChildrenName='" + ChildrenName + '\'' +
                ", ChildrenGender='" + ChildrenGender + '\'' +
                ", ChildrenAge=" + ChildrenAge +
                ", ChildrenOccupation='" + ChildrenOccupation + '\'' +
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
