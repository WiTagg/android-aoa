//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//
package com.witagg.openaoa.antbluetooth.activity;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.witagg.openaoa.antbluetooth.adpter.DeviceInfo;
import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.Config;
import com.witagg.openaoa.antbluetooth.customerview.PolarView;
import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;

public class DeviceDetailActivity extends BaseActivity {
    private SharePreferenceUtil sharePreferenceUtil;

    private View mBack;
    private TextView mTitle;
    private TextView mRight;

    private TextView macTextView;
    private TextView angel1TextView;
    private TextView angel2TextView;
    private PolarView polarView;
    private LocalBroadcastManager broadcastManager;
    private DeviceInfo deviceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        sharePreferenceUtil = new SharePreferenceUtil(mContext);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.device_detail);
        initView();
        setListener();
        getDeviceInfoFromPage();
        registerAd();


    }

    protected void initView() {

        mBack = findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.device_detail);
        mRight = findViewById(R.id.right_btn);
        mRight.setText(R.string.device_setting);
        macTextView = findViewById(R.id.device_detail_mac);
        angel1TextView = findViewById(R.id.device_detail_angel1);

        angel2TextView = findViewById(R.id.device_detail_angel2);

        polarView = findViewById(R.id.clock_polar);

    }

    protected void setListener() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(1,intent);
                finish();
            }
        });

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DeviceSettingActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    protected void getDeviceInfoFromPage() {
        Intent intent = getIntent();
        deviceInfo = new DeviceInfo();
        String mac = intent.getStringExtra("mac");
        String angel1 = intent.getStringExtra("angel1");
        String angel2 = intent.getStringExtra("angel2");
        deviceInfo.setMac(mac);
        deviceInfo.setAngel_1(angel1);
        deviceInfo.setAngel_2(angel2);
        macTextView.setText("mac:" + mac);

    }

    /**
     * register mqtt refresh Device
     * @param number
     * @return
     */
    protected void registerAd() {
        broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Config.REFRESH_DEVICE_AD);

        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter); //set broadcast callback
    }

    /**
     * callback
     * @param number
     * @return
     */
    BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            String mac = bundle.getString("mac");
            String angel1 = bundle.getString("angel1");
            String angel2 = bundle.getString("angel2");
            angel1TextView.setText("Azimuth:" + angel1);
            angel2TextView.setText("Elevtion:" + angel2);

            polarView.setPoint(Double.parseDouble(angel1),Double.parseDouble(angel2));
        }

    };

    public static double convertToDouble(String number) {

        if (number == null || number.length() == 0) {

            return 0;

        }

        try {

            return Double.parseDouble(number);

        } catch (Exception e) {

            return 0;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(mAdDownLoadReceiver);
        } catch (IllegalArgumentException e) {

        }

    }
}
