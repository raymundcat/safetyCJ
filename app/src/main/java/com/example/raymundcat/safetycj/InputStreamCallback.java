package com.example.raymundcat.safetycj;

import android.support.annotation.UiThread;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Raymund on 21/05/2016.
 */
public interface InputStreamCallback {

    @UiThread
    void onResponseJSON(JsonNode node);

    @UiThread
    void onResponseFail();
}
