//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//
package com.witagg.openaoa.antbluetooth.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.witagg.openaoa.antbluetooth.R;



public class BaseFragment extends Fragment  {
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
    /**
     */
    protected <VT extends View> VT getViewById(View view, @IdRes int id) {
        return (VT) view.findViewById(id);
    }


}
