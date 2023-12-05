package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGrnByProcessorId {

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("GRNnumber")
    @Expose
    private String GRNnumber;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;

    @SerializedName("Farmeradress")
    @Expose
    private String Farmeradress;

    @SerializedName("ProcessorId")
    @Expose
    private int ProcessorId;

    @SerializedName("Processor")
    @Expose
    private String Processor;

    @SerializedName("FarmerId")
    @Expose
    private int FarmerId;

    @SerializedName("Quantity")
    @Expose
    private int Quantity;

    @SerializedName("CurrentQty")
    @Expose
    private int CurrentQty;

    @SerializedName("GRNdate")
    @Expose
    private String GRNdate;

    @SerializedName("GRNId")
    @Expose
    private String GRNId;
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

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String GRNnumber) {
        this.GRNnumber = GRNnumber;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String FarmerCode) {
        this.FarmerCode = FarmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String FarmerName) {
        this.FarmerName = FarmerName;
    }

    public String getFarmeradress() {
        return Farmeradress;
    }

    public void setFarmeradress(String Farmeradress) {
        this.Farmeradress = Farmeradress;
    }

    public int getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(int ProcessorId) {
        this.ProcessorId = ProcessorId;
    }

    public String getProcessor() {
        return Processor;
    }

    public void setProcessor(String Processor) {
        this.Processor = Processor;
    }

    public int getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(int FarmerId) {
        this.FarmerId = FarmerId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(int CurrentQty) {
        this.CurrentQty = CurrentQty;
    }

    public String getGRNdate() {
        return GRNdate;
    }

    public void setGRNdate(String GRNdate) {
        this.GRNdate = GRNdate;
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

    public String toString() {
        return GRNnumber;
    }

    public String getGRNId() {
        return GRNId;
    }

    public void setGRNId(String GRNId) {
        this.GRNId = GRNId;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }
}
