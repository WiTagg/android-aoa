//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//

package com.witagg.openaoa.antbluetooth.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.RadioGroup.OnCheckedChangeListener;

import com.witagg.openaoa.antbluetooth.tools.BleUtil;
import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;
import com.witagg.openaoa.antbluetooth.manager.BluetoothManager;
import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.customerview.DrawableText;

public class BluetoothFragment extends BaseFragment{
    private SharePreferenceUtil sharePreferenceUtil;
    private boolean mScanning;
    private Handler mHandler;
    private Handler handler ;
    private TextView macTop_text;
    private EditText mac_edit;
    private RadioGroup radioGroup;
    private RadioButton radioButton_39;
    private RadioButton radioButton_38;
    private RadioButton radioButton_37;
    private Button start_btn;
    private DrawableText showText;
    private int channel;
    //actionbar
    private View mBack;
    private TextView mTitle;
    private TextView mRight;

    private View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        sharePreferenceUtil = new SharePreferenceUtil(mContext);
        myView = inflater.inflate(R.layout.bluetooth_fragment, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    protected void initView() {
        macTop_text = getActivity().findViewById(R.id.mac_top);
        mac_edit = getActivity().findViewById(R.id.mac_edit);

        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroupId);
        radioButton_39 = getActivity().findViewById(R.id.buttonId39);
        radioButton_38 = getActivity().findViewById(R.id.buttonId38);
        radioButton_37 = getActivity().findViewById(R.id.buttonId37);
        radioButton_39.setChecked(true);
        mBack = getActivity().findViewById(R.id.back);
        mTitle = (TextView) getActivity().findViewById(R.id.title);
        mTitle.setText(R.string.device_detail);
        mBack.setVisibility(View.INVISIBLE);
        mTitle.setText(R.string.blue_scan);
        channel = 39;

        start_btn = getActivity().findViewById(R.id.startdrawtext);
        showText = getActivity().findViewById(R.id.show_text);
        macTop_text.setText("06:05:04:");
        if(sharePreferenceUtil.getMac().length() == 0) {
            String mac = BleUtil.getRandomMacStr();
            mac_edit.setText(mac);
            sharePreferenceUtil.setMac(mac);
        } else {
            mac_edit.setText(sharePreferenceUtil.getMac());
        }
        BluetoothManager.getInstance().setContext(mContext);
    }

    protected void initListener() {

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanning = !mScanning;
                startProcess(mScanning);
            }
        });

        radioGroup.setOnCheckedChangeListener(new MyRadioButtonListener());
    }

    public void stopProcess() {
        if (mScanning) {
            startProcess(false);
        }
    }
    public void startProcess(boolean isStop) {
        mScanning = isStop;
        if(mScanning) {
            if(!stringIsMac(getMac())) {
                mScanning = !mScanning;
                Toast.makeText(getContext(), "The MAC address is incorrect!", Toast.LENGTH_LONG).show();
                return;
            }

            if(!sharePreferenceUtil.getMac().equals(mac_edit.getText().toString())) {
                sharePreferenceUtil.setMac(mac_edit.getText().toString());
            }

            start_btn.setText("stop");
            showText.setVisibility(View.VISIBLE);
            showText.start();
            mac_edit.setEnabled(false);
            radioGroup.setEnabled(false);
            radioButton_39.setEnabled(false);
            radioButton_38.setEnabled(false);
            radioButton_37.setEnabled(false);

        } else {
            start_btn.setText("start");
            showText.start();
            showText.setVisibility(View.INVISIBLE);
            mac_edit.setEnabled(true);
            radioGroup.setEnabled(true);
            radioButton_39.setEnabled(true);
            radioButton_38.setEnabled(true);
            radioButton_37.setEnabled(true);
        }
        startAdvertising(mScanning);
    }


    private void startAdvertising(final boolean enable) {
        if (enable) {
            boolean result = BluetoothManager.getInstance().init(getActivity(), 999);
            if (result) {
                BluetoothManager.getInstance().startAdvertising(getMac(), channel);
            } else {
                startProcess(false);
            }
        } else {
            BluetoothManager.getInstance().stopAdvertising();
        }
    }
    private String getMac() {
        return macTop_text.getText().toString() + mac_edit.getText().toString();
    }
    private boolean stringIsMac(String val) {
        String trueMacAddress = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

        if (val.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }

    class MyRadioButtonListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.buttonId39:
                    channel = 39;
                    break;
                case R.id.buttonId38:
                    channel = 38;
                    break;
                case R.id.buttonId37:
                    channel = 37;
                    break;
            }
        }
    }
}
