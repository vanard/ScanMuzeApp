package com.vanard.muze.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String KEY_TOKEN = "TOKEN";
    static final String KEY_MUSEUM_ID = "MUSEUM_ID";
    static final String KEY_MUSEUM_NAME = "MUSEUM_NAME";


    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setTokenUser(Context context, String token){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static String getTokenUser(Context context){
        return getSharedPreference(context).getString(KEY_TOKEN,"");
    }

    public static void setMuseumUser(Context context, String museumId, String museumName){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_MUSEUM_ID, museumId);
        editor.putString(KEY_MUSEUM_NAME, museumName);
        editor.apply();
    }

    public static String getMuseumId(Context context){
        return getSharedPreference(context).getString(KEY_MUSEUM_ID,"");
    }

    public static String getMuseumName(Context context){
        return getSharedPreference(context).getString(KEY_MUSEUM_NAME,"");
    }

    public static void clearData (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_MUSEUM_ID);
        editor.remove(KEY_MUSEUM_NAME);
        editor.apply();
    }
}
