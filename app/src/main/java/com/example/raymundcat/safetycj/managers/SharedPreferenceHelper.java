package com.example.raymundcat.safetycj.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.raymundcat.safetycj.SafetyApplication;

/**
 * Created by Marbeen on 5/22/2016.
 */
public class SharedPreferenceHelper {
        static SharedPreferenceHelper instance;
        SharedPreferences sharedPref;
        Context context;
        String sharedPrefName = "safetyCJDetails";

        private SharedPreferenceHelper(Context context) {
            sharedPref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        }
        public static SharedPreferenceHelper getInstance() {
            if (instance == null) {
                instance = new SharedPreferenceHelper(SafetyApplication.context);
            }
            return instance;
        }

        public SharedPreferenceHelper putBoolean(String key, boolean value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(key, value);
            editor.commit();
            return instance;
        }

        public SharedPreferenceHelper putString(String key, String value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.commit();
            return instance;
        }

        public SharedPreferenceHelper putInt(String key, int value) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(key, value);
            editor.commit();
            return instance;
        }

        public String getString(String key, String defaultValue) {
            return sharedPref.getString(key, defaultValue);
        }

        public boolean getBoolean(String key) {
            return sharedPref.getBoolean(key, false);
        }

        public int getInt(String key, int defaultValue) {
            return sharedPref.getInt(key, defaultValue);
        }

        public String getString(String key) {
            return sharedPref.getString(key, "");
        }

//        public void clear() {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.clear();
//            editor.commit();
//        }
}
