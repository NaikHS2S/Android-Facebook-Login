package com.naikz.android.facebook.integration;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.naikz.android.facebook.integration.util.NksUtil;

public class NksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                NksUtil.getKeyHash(NksApplication.this);
            }
        });
    }
}
