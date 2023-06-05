package com.app.smwc.Common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApplicationLifeCycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApplicationLifeCycle.class.getName();
    private static ApplicationLifeCycle instance;
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();
    Date dtLastDate;
    private int refs;

    public static void init(Application application) {
        if (instance == null) {
            instance = new ApplicationLifeCycle();
            application.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static ApplicationLifeCycle get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static ApplicationLifeCycle get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static ApplicationLifeCycle get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parametrised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return refs > 0;
    }

    public boolean isBackground() {
        return refs == 0;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        if (++refs == 1) {
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }

            Log.e(TAG, "Enter foreground");
        } else {
            Log.e(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (--refs == 0) {
            for (Listener l : listeners) {
                try {
                    l.onBecameBackground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }

            Log.e(TAG, "Enter background");
        } else {
            Log.e(TAG, "still background");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG, "onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityResumed");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(TAG, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "onActivityDestroyed");
    }

    interface Listener {
        void onBecameForeground();

        void onBecameBackground();
    }
} 