package com.naikz.android.facebook.integration.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.naikz.android.R;
import com.naikz.android.facebook.integration.util.NksUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class NksMainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private URL profileImageURL = null;
    private Bitmap profilePicBitmap;
    private Profile userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nks_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        //use Web LoginBehavior to avoid SSO like loginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                userProfile = Profile.getCurrentProfile();

                getSupportActionBar().setTitle(userProfile.getFirstName() + " " + userProfile.getLastName());
                try {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    profileImageURL = new URL("" + userProfile.getProfilePictureUri((int) NksUtil.convertPixelsToDp(width, getApplicationContext()), 400));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            profilePicBitmap = BitmapFactory.decodeStream(profileImageURL.openConnection().getInputStream());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(profilePicBitmap);
                                }
                            });
                        } catch (IOException e) {
                        }
                    }
                }).start();

                 Snackbar.make(findViewById(R.id.mainRelativeLayout), "Welcome\n" + userProfile.getFirstName(), Snackbar.LENGTH_LONG)
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
