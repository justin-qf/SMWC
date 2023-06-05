package com.app.smwc.Common;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SWCApp extends MultiDexApplication {
    private static SWCApp sInstance;
    private AppObserver observer;

    public static SWCApp getsInstance() {
        return sInstance;
    }

    public synchronized static SWCApp getInstance() {
        return sInstance;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        MultiDex.install(this);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        ApplicationLifeCycle.init(sInstance);
        observer = new AppObserver(getApplicationContext());
        printHashKey();
    }

    private void printHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }

    public AppObserver getObserver() {
        return observer;
    }
}