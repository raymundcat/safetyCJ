package com.example.raymundcat.safetycj;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.androidannotations.annotations.EApplication;

/**
 * Created by Raymund on 21/05/2016.
 */
@EApplication
public class SafetyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
