package com.naikz.android.facebook.integration.ui.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.naikz.android.R;

import java.util.Arrays;

public class NksMainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nks_main);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        //use Web LoginBehavior to avoid SSO like loginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                 Snackbar.make(findViewById(R.id.mainRelativeLayout), "onSuccess" + loginResult.getAccessToken().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Logout", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loginManager.logOut();
                            }
                        }).show();
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "onCancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplicationContext(), "onError" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
}
