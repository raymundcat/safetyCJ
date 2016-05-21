package com.example.raymundcat.safetycj;

import android.widget.Toast;

/**
 * Created by Marbeen on 5/22/2016.
 */
public class SToast {

    public static void showDebugToast(String message) {
        if (Constants.isDebug) {
            Toast.makeText(SafetyApplication.context, message,
                    Toast.LENGTH_SHORT).show();
        }

    }
}
