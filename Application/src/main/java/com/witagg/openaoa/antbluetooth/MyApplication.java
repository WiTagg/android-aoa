//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth;

import android.app.Application;
import android.app.Activity;
import android.os.Bundle;

import com.witagg.openaoa.antbluetooth.activity.MainActivity;

public class MyApplication extends Application {
    /**
     * current Acitity num
     */
    private int activityAount = 0;
    private boolean isForeground = false;
    @Override
    public void onCreate() {

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * Activity Lifecycle callback，use for switching between the Foreground and background of the app
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityAount == 0) {
                isForeground = true;
            }
            activityAount++;

        }

        @Override
        public void onActivityResumed(Activity activity) {
        }
        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;
            if (activityAount == 0) {
                isForeground = false;
                if(activity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) activity;
                    mainActivity.startEnterBackgroud();
                }
            }


        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };
}
