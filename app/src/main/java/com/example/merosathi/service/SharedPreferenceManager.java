package com.example.merosathi.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String TOKEN_KEY = "auth_token";

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public static String getBearerToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return "Bearer " + sharedPreferences.getString(TOKEN_KEY, null);
    }

    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }

    public static String getUrl() {
        return "https://merosathi.techenfield.com/";
    }
}
