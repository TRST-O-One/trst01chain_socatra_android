package com.socatra.excutivechain.database.converter;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataTypeConverter {


   public Gson gson = new Gson();

    @TypeConverter
    public static String[] stringToSomeObjectList(String data) {
        String[] stringArray={};
       /* if (data == null) {
            return Collections.emptyList();
        }*/
       try {
           Type listType = new TypeToken<String[]>() {
           }.getType();

           stringArray=new Gson().fromJson(data, listType);
       }catch (Exception ex){
           ex.printStackTrace();
       }
       return  stringArray;
    }

    @TypeConverter
    public static String someObjectListToString(String[] someObjects) {
        try {
            return new Gson().toJson(someObjects);
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}
