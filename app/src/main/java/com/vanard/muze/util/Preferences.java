package com.vanard.muze.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String KEY_TOKEN = "TOKEN";

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

    public static void clearTokenUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
