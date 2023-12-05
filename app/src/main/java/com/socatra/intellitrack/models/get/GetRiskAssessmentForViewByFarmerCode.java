package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRiskAssessmentForViewByFarmerCode {

    @SerializedName("HDRId")
    @Expose
    private Integer hDRId;
    @SerializedName("QuestionCategory")
    @Expose
    private String questionCategory;
    @SerializedName("SerialNo")
    @Expose
    private Integer serialNo;
    @SerializedName("Questions")
    @Expose
    private String questions;
    @SerializedName("Answers")
    @Expose
    private String answers;
    @SerializedName("Rn")
    @Expose
    private String rn;
    @SerializedName("QuestionNo")
    @Expose
    private String questionNo;


    public GetRiskAssessmentForViewByFarmerCode(){


        this.expanded = false;

    }

    private boolean expanded;




    public  boolean isExpanded(){
        return expanded;
    }

    public void  setExpanded(boolean expanded){
        this.expanded = expanded;
    }



    public Integer getHDRId() {
        return hDRId;
    }

    public void setHDRId(Integer hDRId) {
        this.hDRId = hDRId;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

}
