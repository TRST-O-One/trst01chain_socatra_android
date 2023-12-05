package com.socatra.excutivechain;

import android.Manifest;


public class AppConstants {

    public static String App_SHARED_PREF_NAME = "CSP";

    public static final String REGEX_PATTERN_MOBILE_NUMBER = "^(?!(.)\\1{9})([+][9][1]|[9][1]|[0]){0,1}([6-9]{1})([0-9]{9})$";
    public static final String REGEX_PATTERN_LAND_LINE_NUMBER = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}";
    public static final String EMAIL_VALIDATION_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$";

    public static final String REGEX_PATTERN_PANCARD = "[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}";
    public static final String REGEX_PATTERN_PASSPORT = "(([a-zA-Z]{1})\\d{7})";
    public static final String REGEX_PATTERN_VOTERID = "[A-Za-z]{3}[0-9]{7}";
    public static final String REGEX_PATTERN_DRIVINGLICENSE ="[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}";
    public static final String REGEX_PATTERN_PINCODE = "^[1-9][0-9]{5}$";
    public static final String REGEX_PATTERN_CONTINEOUS_THREE_CHARACTERS = "([a-zA-Z0-9])\\1\\1+";

    //Added by prasanna
    public static final String IFSC_CODE_VALIDATION="^[A-Za-z]{4}[a-zA-Z0-9]{7}$";
    public static final String ACCOUNT_NUMBER_VALIDATION="^\\d{9,18}$";

    public static final String DATE_FORMAT_YYYYMMDD_HHMM= "yyyyMMdd_HHmm";
    public static final String DATE_FORMAT_MMM_DD_YYYY = "MMM dd, yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY_WITH_SLASH = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DDMMMYYYY = "ddMMMyyyy";
    public static final String DATE_FORMAT_YYYY_MM_DD= "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final String PERMISSION_DENIED_EXPLANATION = "Permission was denied, but is needed for core functionality";
    public static final String PERMISSION_RATIONALE = "Location permission is needed for core functionality";

    public static final int IMAGE_CAPTURE_REQUEST = 10;
    public static final int IMAGE_GALLERY_REQUEST = 11;

    public static final String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.ACCESS_FINE_LOCATION };
    public static final int ALL_PERMISSION_CODE = 100;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int READ_PERMISSION_CODE = 102;
    public static final int WRITE_PERMISSION_CODE = 103;
    public static final int LOCATION_PERMISSION_CODE = 104;
}
