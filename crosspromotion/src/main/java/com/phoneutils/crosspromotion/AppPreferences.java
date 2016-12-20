package com.phoneutils.crosspromotion;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashish123 on 27/6/15.
 */
public class AppPreferences {

    public AppPreferences() {
    }

    private static final String APP_SHARED_PREFERENCE = "app_preferences";

    public static final String KEY_APPS_JSON = "key_apps_json";
    public static final String KEY_LAST_SYNC_TIME = "key_last_sync_time";
    public static final String KEY_URL = "key_server_url";
    public static final java.lang.String KEY_CATEGORY = "key_category_key";
    public static final java.lang.String KEY_POLICY_STATUS = "key_status_policy";


    public static void setSharedPreference(Context ctx, String Key, String Value) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Value);
        editor.apply();
    }


    public static void setBooleanSharedPreference(Context ctx, String key, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setLongSharedPreference(Context ctx, String key, long value) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }


    public static String getSharedPreference(Context ctx, String Key) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        if (pref.contains(Key)) {

            return pref.getString(Key, "");
        } else
            return null;
    }

    public static String getSharedPreference(Context ctx, String Key, String defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        if (pref.contains(Key)) {

            return pref.getString(Key, "");
        } else
            return defaultValue;
    }


    public static boolean getBooleanSharedPreference(Context ctx, String Key, Boolean defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        if (pref.contains(Key)) {
            return pref.getBoolean(Key, defaultValue);
        } else
            return defaultValue;
    }

    public static long getLongSharedPreference(Context ctx, String Key, long defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        if (pref.contains(Key)) {
            return pref.getLong(Key, defaultValue);
        } else
            return defaultValue;
    }

}
