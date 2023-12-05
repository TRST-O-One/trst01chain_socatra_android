
package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransalteLanguageWordsByLanguageId {

    @SerializedName("languageid")
    @Expose
    private Integer languageid;
    @SerializedName("SelectedLanguage")
    @Expose
    private String selectedLanguage;
    @SerializedName("SelectedWord")
    @Expose
    private String selectedWord;
    @SerializedName("TranslatedWord")
    @Expose
    private String translatedWord;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ScreenName")
    @Expose
    private String screenName;

    public Integer getLanguageid() {
        return languageid;
    }

    public void setLanguageid(Integer languageid) {
        this.languageid = languageid;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(String selectedWord) {
        this.selectedWord = selectedWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

}
