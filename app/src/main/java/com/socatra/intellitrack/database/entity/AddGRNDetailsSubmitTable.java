package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity
public class AddGRNDetailsSubmitTable {


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
    @SerializedName("FromProcessorId")
    @Expose
    private String fromProcessorId;
    @SerializedName("FromDealerId")
    @Expose
    private String fromDealerId;
    @SerializedName("ToDealerId")
    @Expose
    private String toDealerId;
    @SerializedName("FarmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("GRNDocument")
    @Expose
    private String gRNDocument;
    @SerializedName("InvoiceId")
    @Expose
    private String invoiceId;
    @SerializedName("GRNdate")
    @Expose
    private String gRNdate;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("ModeofTransport")
    @Expose
    private String modeofTransport;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private String createdByUserId;
    @SerializedName("UpdatedByUserId")
    @Expose
    private String updatedByUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromProcessorId() {
        return fromProcessorId;
    }

    public void setFromProcessorId(String fromProcessorId) {
        this.fromProcessorId = fromProcessorId;
    }

    public String getFromDealerId() {
        return fromDealerId;
    }

    public void setFromDealerId(String fromDealerId) {
        this.fromDealerId = fromDealerId;
    }

    public String getToDealerId() {
        return toDealerId;
    }

    public void setToDealerId(String toDealerId) {
        this.toDealerId = toDealerId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getGRNDocument() {
        return gRNDocument;
    }

    public void setGRNDocument(String gRNDocument) {
        this.gRNDocument = gRNDocument;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getGRNdate() {
        return gRNdate;
    }

    public void setGRNdate(String gRNdate) {
        this.gRNdate = gRNdate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModeofTransport() {
        return modeofTransport;
    }

    public void setModeofTransport(String modeofTransport) {
        this.modeofTransport = modeofTransport;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }
}