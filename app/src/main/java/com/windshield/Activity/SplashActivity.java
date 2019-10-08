package com.windshield.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.windshield.Activity.Constant.RuntimePermissionActivity;
import com.windshield.R;

public class SplashActivity extends RuntimePermissionActivity {

    private static final int REQUEST_PERMISSION = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        splashScreen();
        SplashActivity.super.requestAppPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CALL_PHONE,
                },R.string.runtime_permissions_txt,
                REQUEST_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        splashScreen();
    }

    public void splashScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finishAffinity();
            }
        },1500);
    }
}
