//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.witagg.openaoa.antbluetooth.Config;
import com.witagg.openaoa.antbluetooth.activity.DeviceDetailActivity;
import com.witagg.openaoa.antbluetooth.manager.MqttClient;
import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;
import com.witagg.openaoa.antbluetooth.adpter.DeviceInfo;
import com.witagg.openaoa.antbluetooth.adpter.DeviceListAdapter;
import com.witagg.openaoa.antbluetooth.adpter.DeviceType;
import com.witagg.openaoa.antbluetooth.customerview.DrawableText;
import android.content.res.ColorStateList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class MqttFragment extends BaseFragment{
    private SharePreferenceUtil sharePreferenceUtil;
    private boolean mScanning;

    private EditText mqtt_ip_edit;
    private EditText mqtt_port_edit;
    private EditText mqtt_topic_edit;
    private TextView mqtt_serverstatus_textview;
    private TextView mqtt_subscribestatus_textview;
    private TextView mqtt_new_message_textview;
    private ColorStateList csl;
   // private Button start_btn;
    private DrawableText startDrawableText;
    //mqtt
    private List<DeviceType> mData;
    //actionbar
    private View mBack;
    private TextView mTitle;
    private TextView mRight;
    private View myView;
    private ListView listView;
    private DeviceListAdapter deviceListAdapter;
    private boolean isSendRefreshDeviceAd;
    private String selectDeviceMac;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        sharePreferenceUtil = new SharePreferenceUtil(mContext);
        myView = inflater.inflate(R.layout.mqtt_fragment, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1) {
            isSendRefreshDeviceAd = false;
            selectDeviceMac = "";
        }

    }

    protected void initView() {
        mqtt_ip_edit = getActivity().findViewById(R.id.mqtt_ip_edit);
        mqtt_port_edit = getActivity().findViewById(R.id.mqtt_port_edit);
        mqtt_topic_edit = getActivity().findViewById(R.id.mqtt_topic_edit);
        mqtt_new_message_textview = getActivity().findViewById(R.id.mqtt_new_message_textview);
        mqtt_serverstatus_textview = getActivity().findViewById(R.id.mqtt_server_status);
        mqtt_subscribestatus_textview = getActivity().findViewById(R.id.mqtt_subscribe_status);
        csl = mContext.getResources().getColorStateList(R.color.common_text_red);
        mqtt_serverstatus_textview.setTextColor(csl);
        mqtt_subscribestatus_textview.setTextColor(csl);
        //start_btn = getActivity().findViewById(R.id.mqtt_start_btn);
        startDrawableText = getActivity().findViewById(R.id.drawtext);


        View actionBar = getActivity().findViewById(R.id.mqtt_bar);
        mBack = getViewById(actionBar,R.id.back);
        mTitle = getViewById(actionBar,R.id.title);
        //mqtt_new_message
        //_textview.setText("{\"recv_time\": \"2022-02-10 10:31:05\", \"mac\": \"B1:06:88:51:02:01\",\"seq\": 297001, \"rssi\": -81, \"freq\": 2480000000, \"aoa_2d\": [55, 278]}\")");
        mBack.setVisibility(View.INVISIBLE);
        mTitle.setText(R.string.mqtt_titile);

        if(sharePreferenceUtil.getMqttIp().length() != 0) {
            mqtt_ip_edit.setText(sharePreferenceUtil.getMqttIp());
        }

        if(sharePreferenceUtil.getMqttPort().length() != 0) {
            mqtt_port_edit.setText(sharePreferenceUtil.getMqttPort());
        }

        if(sharePreferenceUtil.getMqttTopic().length() != 0) {
            mqtt_topic_edit.setText(sharePreferenceUtil.getMqttTopic());
        }

        MqttClient.getInstance().setContext(mContext);

        listView = getActivity().findViewById(R.id.list_view);

        mData = new ArrayList<DeviceType>();
        DeviceType type = new DeviceType("Device List");

        // value for testing
//        DeviceInfo deviceInfo = new DeviceInfo();
//        deviceInfo.setMac("12222222");
//        deviceInfo.setAngel_1("60");
//        deviceInfo.setAngel_2("32");
//        type.addItem(deviceInfo);

        mData.add(type);
        deviceListAdapter=new DeviceListAdapter(getContext(),mData);
        listView.setAdapter(deviceListAdapter);

    }

    protected void initListener() {

        startDrawableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mScanning = !mScanning;
                startProcess(mScanning);

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    return;
                }
                DeviceType devicetype = mData.get(0);
                DeviceInfo deviceInfo = (DeviceInfo) devicetype.getItem(position);
                Intent intent = new Intent(mContext, DeviceDetailActivity.class);
                if(deviceInfo.getMac().length() != 0) {
                    isSendRefreshDeviceAd = true;
                    selectDeviceMac = deviceInfo.getMac();
                    intent.putExtra("mac", deviceInfo.getMac());
                    intent.putExtra("angel1", deviceInfo.getAngel_1());
                    intent.putExtra("angel2", deviceInfo.getAngel_2());
                    startActivityForResult(intent,1);
                }
            }
        });
    }

    public  void stopProcess() {
        if (mScanning) {
            startProcess(false);
        }
    }
    public void startProcess(boolean isStop) {
        mScanning = isStop;
        if(mScanning) {


            if(!isIpAddr(mqtt_ip_edit.getText().toString())) {
                mScanning = !mScanning;
                Toast.makeText(mContext, "The mqtt ip is incorrect!", Toast.LENGTH_LONG).show();
                return;
            }
            if(!isNetPort(mqtt_port_edit.getText().toString())) {
                mScanning = !mScanning;
                Toast.makeText(mContext, "The mqtt port is incorrect!", Toast.LENGTH_LONG).show();
                return;
            }

            if(mqtt_topic_edit.getText().toString().length() == 0) {
                mScanning = !mScanning;
                Toast.makeText(mContext, "The mqtt topic cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }

            if(!sharePreferenceUtil.getMqttIp().equals(mqtt_ip_edit.getText().toString())) {
                sharePreferenceUtil.setMqttIp(mqtt_ip_edit.getText().toString());
            }
            if(!sharePreferenceUtil.getMqttPort().equals(mqtt_port_edit.getText().toString())) {
                sharePreferenceUtil.setMqttPort(mqtt_port_edit.getText().toString());
            }
            if(!sharePreferenceUtil.getMqttTopic().equals(mqtt_topic_edit.getText().toString())) {
                sharePreferenceUtil.setMqttTopic(mqtt_topic_edit.getText().toString());
            }

            startDrawableText.setText("stop");
            startDrawableText.start();
            mqtt_ip_edit.setEnabled(false);
            mqtt_port_edit.setEnabled(false);
            mqtt_topic_edit.setEnabled(false);

        } else {
            startDrawableText.setText("start");
            startDrawableText.stop();
            mqtt_ip_edit.setEnabled(true);
            mqtt_port_edit.setEnabled(true);
            mqtt_topic_edit.setEnabled(true);
        }
        startMqtt(mScanning);
    }
    private void startMqtt(final boolean enable) {
        if (enable) {
            MqttClient.getInstance().setServer_status_textview(mqtt_serverstatus_textview);
            MqttClient.getInstance().setSubscribe_status_textview(mqtt_subscribestatus_textview);
            MqttClient.getInstance().setMqttIp(mqtt_ip_edit.getText().toString());
            MqttClient.getInstance().setMqttPort(mqtt_port_edit.getText().toString());
            MqttClient.getInstance().setMqttTopic(mqtt_topic_edit.getText().toString());
            MqttClient.getInstance().connectMQTT();

            MqttClient.getInstance().getmMqttClient().setCallback(this.mqttCallback);

        } else {
            DeviceType type = mData.get(0);
            type.clear();
            MqttClient.getInstance().stopMqtt();
            deviceListAdapter.setDatas(mData);
            mqtt_serverstatus_textview.setText("UnConnected");
            mqtt_serverstatus_textview.setTextColor(csl);

            mqtt_subscribestatus_textview.setText("UnSubscribe");
            mqtt_subscribestatus_textview.setTextColor(csl);

            mqtt_new_message_textview.setText("");
        }
    }

    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

            mqtt_serverstatus_textview.setText("UnConnected");
            mqtt_serverstatus_textview.setTextColor(csl);

            mqtt_subscribestatus_textview.setText("UnSubscribe");
            mqtt_subscribestatus_textview.setTextColor(csl);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String jsonStr = new String(message.getPayload());
            //jsonStr = "{\"recv_time\": \"2022-02-10 10:31:05\", \"mac\": \"B1:06:88:51:02:01\",\"seq\": 297001, \"rssi\": -81, \"freq\": 2480000000, \"aoa_2d\": [55, 278]}\")";

            mqtt_new_message_textview.setText(jsonStr);
            DeviceInfo deviceInfo = JSON.parseObject(jsonStr, DeviceInfo.class);
            DeviceType type = mData.get(0);

            type.addItem(deviceInfo);
            deviceListAdapter.setDatas(mData);
            if(isSendRefreshDeviceAd && selectDeviceMac.length() != 0) {
                if (selectDeviceMac.equals(deviceInfo.getMac())) {

                    Intent intents = new Intent(Config.REFRESH_DEVICE_AD);
                    intents.putExtra("mac", deviceInfo.getMac());
                    intents.putExtra("angel1", deviceInfo.getAngel_1());
                    intents.putExtra("angel2", deviceInfo.getAngel_2());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intents);
                }

            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private boolean isNetPort(String port) {
        boolean flag = false;
        try {
            int port_int = Integer.parseInt(port);
            if (port_int >= 0 && port_int <= 65535) {
                flag = true;
            }
        } catch (Exception e) {
            return false;
        }

        return flag;
    }

    private boolean isIpAddr(String ipStr) {
        if (ipStr.length() < 7 || ipStr.length() > 15) return false;

        String[] ipArray = ipStr.split("\\.");
        if (ipArray.length != 4) return  false;
        for (int i = 0; i < ipArray.length; i++) {
            try {
                int number = Integer.parseInt(ipArray[i]);
                if (number < 0 || number > 255) return false;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
