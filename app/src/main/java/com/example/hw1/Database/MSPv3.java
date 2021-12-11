package com.example.hw1.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class MSPv3 {
    private static final String SP_FILE = "RaceCarApp";
    private SharedPreferences preferences;
    private static MSPv3 msPv3;

    private MSPv3(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSPv3 getInstance(Context context) {
        if (msPv3 == null) {
            msPv3 = new MSPv3(context);
        }
        return msPv3;
    }

    public int getIntSP(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putIntSP(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getStringSP(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putStringSP(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}