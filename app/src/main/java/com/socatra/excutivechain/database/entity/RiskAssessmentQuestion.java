package com.socatra.excutivechain.database.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class RiskAssessmentQuestion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("QuestionId")
    @Expose(serialize = false, deserialize = false)
    private int QuestionId;

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("QuestionCategory")
    @Expose
    private String QuestionCategory;

    @SerializedName("SerialNo")
    @Expose
    private int SerialNo;

    @SerializedName("Questions")
    @Expose
    private String Questions;

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

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int QuestionId) {
        this.QuestionId = QuestionId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getQuestionCategory() {
        return QuestionCategory;
    }

    public void setQuestionCategory(String QuestionCategory) {
        this.QuestionCategory = QuestionCategory;
    }

    public int getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(int SerialNo) {
        this.SerialNo = SerialNo;
    }

    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String Questions) {
        this.Questions = Questions;
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
}
