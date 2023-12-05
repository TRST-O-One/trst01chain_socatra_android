package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrnByFarmercode {

    @SerializedName("Id")
    @Expose
    private Integer Id;

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
    private Object Farmeradress;
    @SerializedName("ProcessorId")
    @Expose
    private Object ProcessorId;
    @SerializedName("Dealer")
    @Expose
    private String Dealer;

    @SerializedName("DRCValue")
    @Expose
    private String DRCValue;
    @SerializedName("DealerId")
    @Expose
    private Integer DealerId;
    @SerializedName("FarmerId")
    @Expose
    private Integer FarmerId;
    @SerializedName("Quantity")
    @Expose
    private Integer Quantity;
    @SerializedName("CurrentQty")
    @Expose
    private Integer CurrentQty;
    @SerializedName("GRNdate")
    @Expose
    private String GRNdate;


    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;

    @SerializedName("GRNDocument")
    @Expose
    private String GRNDocument;
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

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String gRNnumber) {
        this.GRNnumber = gRNnumber;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.FarmerCode = farmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        this.FarmerName = farmerName;
    }

    public String getDRCValue() {
        return DRCValue;
    }

    public void setDRCValue(String DRCValue) {
        this.DRCValue = DRCValue;
    }

    public Object getFarmeradress() {
        return Farmeradress;
    }

    public void setFarmeradress(Object farmeradress) {
        this.Farmeradress = farmeradress;
    }

    public Object getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(Object processorId) {
        this.ProcessorId = processorId;
    }

    public String getDealer() {
        return Dealer;
    }

    public void setDealer(String dealer) {
        this.Dealer = dealer;
    }

    public Integer getDealerId() {
        return DealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.DealerId = dealerId;
    }

    public Integer getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.FarmerId = farmerId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        this.Quantity = quantity;
    }

    public Integer getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(Integer currentQty) {
        this.CurrentQty = currentQty;
    }

    public String getGRNdate() {
        return GRNdate;
    }

    public void setGRNdate(String gRNdate) {
        this.GRNdate = gRNdate;
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        this.IsActive = isActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.UpdatedDate = updatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.UpdatedByUserId = updatedByUserId;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        PrimaryContactNo = primaryContactNo;
    }

    public String getGRNDocument() {
        return GRNDocument;
    }

    public void setGRNDocument(String GRNDocument) {
        this.GRNDocument = GRNDocument;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }
}