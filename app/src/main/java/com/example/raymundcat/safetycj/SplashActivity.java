package com.example.raymundcat.safetycj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raymund on 21/05/2016.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    @ViewById(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @AfterViews
    void afterViews(){

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("", "Login results: " + loginResult.getAccessToken().getUserId());
                MainActivity_.intent(SplashActivity.this).start();
            }

            @Override
            public void onCancel() {
                Log.i("", "Login results: Cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("", "Login results: " + e);
            }
        });

        if (isLoggedIn()){
            MainActivity_.intent(SplashActivity.this).start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
