package com.socatra.intellitrack.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String DOB = "dob";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String PROVIDER_ID = "id_proof";
    public static final String OTP_STATUS = "otp_status";
    public static final String DOCUMENTS = "user_documents";
    public static final String ACTIVITYNAME = "Activity";
    public static final String LOGIN_TYPE = "auth_level";

    public static final String PASSWORD = "password";
    public static final String BranchID = "branchID";
    public static final String BranchCurency = "currency";
    public static final String BranchName = "branchName";
    public static final String CountryID = "CountryId";
    public static final String OrderType = "OrderType";
    public static final String StrPaymentRegID = "registrationId";

    //user informatio
    public static String UserName = "username";
    public static String Email = "email";
    public static String USER_ID = "user_id";
    public static String LOG_IN = "login";
    public static String LOG_OUT = "logout";
    public static String Language = "lang";
    public static String DATE_KEY = "date_check";
    public static String DATE_KEY1 = "date_check1";
    public static String UserFullName = "fullname";
    public static String Number = "number";

    public static String Image = "file";


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public void saveString(String strKey, String strValue) {
        editor.putString(strKey, strValue);
        editor.commit();
    }

    public String getDatePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(DATE_KEY, null);
    }

    public void setDatePreference(String datePreference) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATE_KEY, datePreference);
        editor.apply();
    }

    public String getDate1Preference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(DATE_KEY1, null);
    }

    public void setDate1Preference(String datePreference) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATE_KEY1, datePreference);
        editor.apply();
    }


    public void saveInt(String strKey, int value) {
        editor.putInt(strKey, value);
        editor.commit();
    }

    public void saveLong(String strKey, Long value) {
        editor.putLong(strKey, value);
        editor.commit();
    }

    public void saveFloat(String strKey, float value) {
        editor.putFloat(strKey, value);
        editor.commit();
    }

    public void saveDouble(String strKey, String value) {
        editor.putString(strKey, value);
        editor.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        editor.putBoolean(strKey, value);
        editor.commit();
    }

    public void removeFromPreference(String strKey) {
        editor.remove(strKey);
    }

    public String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getIntFromPreference(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public long getLongFromPreference(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public float getFloatFromPreference(String strKey, float defaultValue) {
        return preferences.getFloat(strKey, defaultValue);
    }

    public double getDoubleFromPreference(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, "" + defaultValue));
    }

    public String getLanguage() {
        return preferences.getString(Language, "");
    }

    public void setLanguage(String lang) {
        // Storing login value as TRUE
        // Storing name in pref
        editor.putString(Language, lang);
        editor.commit();
    }

  /*  public void logOut() {

        editor.clear();
        editor.commit();
//        setApp_runFirst("NO");

    }
*/
//    public void logOut() {
//        editor.remove(API_KEY);
//        editor.remove(UserName);
//        editor.remove(Email);
//        editor.remove(Mobile);
//        editor.remove(IMAGE);
//        editor.remove(USER_ID);
//        editor.remove(LOG_OUT);
//        editor.remove(LOG_IN);
//        editor.remove(onBoardingDisplayed);
//        editor.remove(DOB);
//        editor.remove(GENDER);
//        editor.remove(ADDRESS);
//        editor.remove(PROVIDER_ID);
//        editor.remove(OTP_STATUS);
//        editor.remove(DOCUMENTS);
//        editor.remove(ACTIVITYNAME);
//        editor.remove(LOGIN_TYPE);
////        editor.remove(Language);
//
//        editor.clear();
//        editor.commit();
//    }

}