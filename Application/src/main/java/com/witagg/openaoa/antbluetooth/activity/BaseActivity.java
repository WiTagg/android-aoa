//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;



public class BaseActivity extends Activity {
    private ProgressDialog dialog;
    protected Context mContext;

    public void showDialog(String content) {
        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(0);
        dialog.setMessage(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void killDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }




}
