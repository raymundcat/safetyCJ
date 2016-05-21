package com.example.raymundcat.safetycj;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.androidannotations.annotations.EApplication;

/**
 * Created by Raymund on 21/05/2016.
 */
@EApplication
public class SafetyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        context = getApplicationContext();
    }
}
