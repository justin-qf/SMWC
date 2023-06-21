package com.app.smwc.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.app.frimline.views.Utils;
import com.app.omcsalesapp.Views.DialogToast;
import com.app.smwc.Common.HELPER;
import com.app.smwc.Common.NetworkChangeReceiver;
import com.app.smwc.Common.SWCApp;
import com.app.smwc.R;
import com.app.ssn.Common.Pref;
import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public Activity act;
    public Pref prefManager;
    public SWCApp app;
    public Gson gson;
    private boolean LIVE_MODE = false;
    public static Activity staticAct;
    Window window;
    public BroadcastReceiver mNetworkReceiver;
    public static DialogToast noConnectionAlertDialog;
    private static Dialog noInternetConnectionAlertDialog;

    public BaseActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        window = this.getWindow();
        gson = new Gson();
        staticAct = this;
        prefManager = new Pref(this);
        app = (SWCApp) getApplicationContext();
        app.getObserver().addObserver(this);
        noConnectionAlertDialog = new DialogToast(this);
        noInternetConnectionAlertDialog = new Dialog(this);
        noInternetConnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (LIVE_MODE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {
    }

    public static void InternetError(boolean value) {
        if (!staticAct.isDestroyed() && !staticAct.isFinishing()) {
            if (value) {
                if (noInternetConnectionAlertDialog.isShowing()) {
                    noInternetConnectionAlertDialog.dismiss();
                }
            } else {
                showNoConnectionDialog();
            }
        }
    }

    private static void showNoConnectionDialog() {
        if (!noInternetConnectionAlertDialog.isShowing()) {
            noInternetConnectionAlertDialog.setContentView(R.layout.noconnectionlayout);
            noInternetConnectionAlertDialog.setCancelable(false);
            noInternetConnectionAlertDialog.findViewById(R.id.closeBtn).setOnClickListener(view -> {
                noInternetConnectionAlertDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    staticAct.startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                } else {
                    staticAct.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            });
            noInternetConnectionAlertDialog.show();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getWindow().getDecorView().getRootView().getWindowToken(),
                    0
            );
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void hideKeyboard(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener((View v, MotionEvent event) -> {
                Utils.INSTANCE.hideKeyboard(act);
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(innerView);
            }
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
