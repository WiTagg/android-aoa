//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//
package com.witagg.openaoa.antbluetooth.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;
import com.witagg.openaoa.antbluetooth.R;

public class DeviceSettingActivity extends BaseActivity {
    private SharePreferenceUtil sharePreferenceUtil;
    private View mBack;
    private TextView mTitle;
    private TextView mRight;

    private TextView kalman_strength_text;
    private TextView tail_length_text;
    private SeekBar  kalman_seekbar;
    private SeekBar  tail_seekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        sharePreferenceUtil = new SharePreferenceUtil(mContext);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.device_setting_layout);
        initView();
        setListener();

    }

    protected void initView() {

        mBack = findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.device_setting);

        kalman_strength_text = findViewById(R.id.kalman_strength);
        kalman_seekbar = findViewById(R.id.kalman_seekBar);
        kalman_seekbar.setKeyProgressIncrement(1);
        int kalman_progress = sharePreferenceUtil.getKalmanStrength();
        kalman_seekbar.setProgress(kalman_progress);
        setKalman_strength_text(kalman_progress);

        tail_length_text = findViewById(R.id.tail_length);
        tail_seekbar = findViewById(R.id.tail_lengh_seekBar);
        tail_seekbar.setKeyProgressIncrement(1);
        int tail_progress = sharePreferenceUtil.getTailLength();
        tail_seekbar.setProgress(tail_progress);

        setTail_length_text(tail_progress);
    }

    protected void setListener() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });

        kalman_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                setKalman_strength_text(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharePreferenceUtil.setKalmanStrength(seekBar.getProgress());
            }
        });

        tail_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                setTail_length_text(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharePreferenceUtil.setTailLength(seekBar.getProgress());
            }
        });
    }

    private void setKalman_strength_text(int progress) {
        String kalman_text = "Kalman Strength:";
        switch (progress) {
            case 0:
                kalman_text = kalman_text + "Off";
                break;
            case 1:
                kalman_text = kalman_text + "Very Low";
                break;
            case 2:
                kalman_text = kalman_text + "Low";
                break;
            case 3:
                kalman_text = kalman_text + "Medium";
                break;
            case 4:
                kalman_text = kalman_text + "High";
                break;
            case 5:
                kalman_text = kalman_text + "Very High";
                break;
            case 6:
                kalman_text = kalman_text + "Max";
                break;
            default:
                kalman_text = kalman_text + "Max";
                break;
        }
        kalman_strength_text.setText(kalman_text);
    }

    private void setTail_length_text(int progress) {
        String tail_text = "Tail Length:" + String.valueOf(progress);
        tail_length_text.setText(tail_text);
    }

}
