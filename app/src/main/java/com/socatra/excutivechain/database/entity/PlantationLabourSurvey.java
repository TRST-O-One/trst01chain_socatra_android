package com.socatra.excutivechain.database.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class PlantationLabourSurvey {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    private int Id;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;




    @SerializedName("PlantationCode")
    @Expose
    private String PlantationCode;

    @SerializedName("NoOfFieldWorkers")
    @Expose
    private Integer NoOfFieldWorkers;
    @SerializedName("NoOfMaleWorkers")
    @Expose
    private Integer NoOfMaleWorkers;
    @SerializedName("NoOfFemaleWorkers")
    @Expose
    private Integer NoOfFemaleWorkers;
    @SerializedName("NoOfResident")
    @Expose
    private Integer NoOfResident;
    @SerializedName("NoOfMigrant")
    @Expose
    private Integer NoOfMigrant;
    @SerializedName("OccupationOfChildren")
    @Expose
    private String OccupationOfChildren;
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

    /*public Integer getPlantationId() {
        return PlantationId;
    }

    public void setPlantationId(Integer PlantationId) {
        this.PlantationId = PlantationId;
    }*/

    public String getPlantationCode() {
        return PlantationCode;
    }

    public void setPlantationCode(String plantationCode) {
        PlantationCode = plantationCode;
    }

    public Integer getNoOfFieldWorkers() {
        return NoOfFieldWorkers;
    }

    public void setNoOfFieldWorkers(Integer NoOfFieldWorkers) {
        this.NoOfFieldWorkers = NoOfFieldWorkers;
    }

    public Integer getNoOfMaleWorkers() {
        return NoOfMaleWorkers;
    }

    public void setNoOfMaleWorkers(Integer NoOfMaleWorkers) {
        this.NoOfMaleWorkers = NoOfMaleWorkers;
    }



    public Integer getNoOfFemaleWorkers() {
        return NoOfFemaleWorkers;
    }

    public void setNoOfFemaleWorkers(Integer NoOfFemaleWorkers) {
        this.NoOfFemaleWorkers = NoOfFemaleWorkers;
    }

    public Integer getNoOfResident() {
        return NoOfResident;
    }

    public void setNoOfResident(Integer NoOfResident) {
        this.NoOfResident = NoOfResident;
    }

    public Integer getNoOfMigrant() {
        return NoOfMigrant;
    }

    public void setNoOfMigrant(Integer NoOfMigrant) {
        this.NoOfMigrant = NoOfMigrant;
    }

    public String getOccupationOfChildren() {
        return OccupationOfChildren;
    }

    public void setOccupationOfChildren(String OccupationOfChildren) {
        this.OccupationOfChildren = OccupationOfChildren;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getServerSync() {
        return ServerSync;
    }

    public void setServerSync(String serverSync) {
        ServerSync = serverSync;
    }
}
