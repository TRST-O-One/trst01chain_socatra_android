package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GetDrcByProcessor {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("DRCDocument")
    @Expose
    private String DRCDocument;
    @SerializedName("DRCNo")
    @Expose
    private String DRCNo;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getDRCDocument() {
        return DRCDocument;
    }

    public void setDRCDocument(String dRCDocument) {
        this.DRCDocument = dRCDocument;
    }

    public String getDRCNo() {
        return DRCNo;
    }

    public void setDRCNo(String dRCNo) {
        this.DRCNo = dRCNo;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

}