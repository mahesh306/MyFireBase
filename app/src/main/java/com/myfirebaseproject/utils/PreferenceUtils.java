package com.myfirebaseproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static SharedPreferences pref;

    public PreferenceUtils(Context context) {
        pref = context.getSharedPreferences("Tracker", Context.MODE_PRIVATE);
    }

    public void setServiceStatus(String status){
        pref.edit().putString("ServiceStatus", status).apply();
    }

    public String getServiceStatus(){
        return pref.getString("ServiceStatus","");
    }


    public void setName(String Name){
        pref.edit().putString("Name", Name).apply();
    }

    public String getName(){
        return pref.getString("Name","");
    }
}
