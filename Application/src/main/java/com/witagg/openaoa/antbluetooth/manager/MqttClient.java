//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//

package com.witagg.openaoa.antbluetooth.manager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.ColorStateList;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;


import com.witagg.openaoa.antbluetooth.R;


public class MqttClient {
    private static MqttClient instance;
    private Context context;
    private boolean isConnected;
    private String mqttIp;
    private String mqttPort;
    private String mqttTopic;
    private TextView server_status_textview;
    private TextView subscribe_status_textview;
    private static class SingletonHolder {
        static final MqttClient instance = new MqttClient();
    }

    public synchronized static MqttClient getInstance() {
        return MqttClient.SingletonHolder.instance;
    }
    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }


    private MqttAndroidClient mMqttClient;
    private static final String TAG = "MqttClient";

    public void setMqttIp(String mqttIp) {
        this.mqttIp = mqttIp;
    }

    public void setMqttPort(String mqttPort) {
        this.mqttPort = mqttPort;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    public void setServer_status_textview(TextView server_status_textview) {
        this.server_status_textview = server_status_textview;
    }

    public void setSubscribe_status_textview(TextView subscribe_status_textview) {
        this.subscribe_status_textview = subscribe_status_textview;
    }

    public MqttAndroidClient getmMqttClient() {
        return mMqttClient;
    }

    //connect mqtt server
    public void connectMQTT() {
        if(this.mqttIp.length() == 0 || this.mqttPort.length() == 0){
            Toast.makeText(context, "mqtt ip or port cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        String serverHost = "tcp://" + mqttIp + ":" + mqttPort;
        //clientId, this clientId must be unique
        String clientId = "witagg" + System.currentTimeMillis();
        mMqttClient = new MqttAndroidClient(context, serverHost, clientId);
        //Connection parameters
        MqttConnectOptions options;
        options = new MqttConnectOptions();
        //set auto reconnect
        options.setAutomaticReconnect(true);
        // cacahe,
        options.setCleanSession(true);
        // set timeout
        options.setConnectionTimeout(15);

        options.setKeepAliveInterval(15);



        try {
            //start connect
            mMqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Connected Success");
                    isConnected = true;
                    if (server_status_textview != null) {
                        server_status_textview.setText("Connected Success");
                        server_status_textview.setTextColor(Color.GREEN);
                    }
                    subscribeTopic(mqttTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: Connected Failed");
                    if (server_status_textview != null) {
                        server_status_textview.setText("Connected Failed");
                        ColorStateList csl = context.getResources().getColorStateList(R.color.common_text_red);

                        server_status_textview.setTextColor(csl);
                    }
                }
            });

        } catch (MqttException e) {
            if (server_status_textview != null) {
                server_status_textview.setText("Connected Failed");
                ColorStateList csl = context.getResources().getColorStateList(R.color.common_text_red);

                server_status_textview.setTextColor(csl);
            }
            e.printStackTrace();
        }

    }
    public void subscribeTopic(String topic) {
        if (topic.length() == 0) {
            return;
        }
        try {
            mMqttClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Subscribe Success");
                    if (subscribe_status_textview != null) {
                        subscribe_status_textview.setText("Subscribe Success");
                        subscribe_status_textview.setTextColor(Color.GREEN);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    if (subscribe_status_textview != null) {
                        subscribe_status_textview.setText("Subscribe Failed");
                        ColorStateList csl = context.getResources().getColorStateList(R.color.common_text_red);

                        subscribe_status_textview.setTextColor(csl);
                    }
                    String msg = "";
                    if (exception != null) {
                        msg = "," + exception.getMessage();
                        exception.printStackTrace();
                    } else {
                        msg = "unknown error！";
                    }
                }
            });

        } catch (MqttException e) {
            if (subscribe_status_textview != null) {
                subscribe_status_textview.setText("Subscribe Failed");
                ColorStateList csl = context.getResources().getColorStateList(R.color.common_text_red);

                subscribe_status_textview.setTextColor(csl);
            }
            Log.i(TAG, "Subscribe>topic:" + topic);
            String msg = "";
            if (e != null) {
                msg = "," + e.getMessage();
                e.printStackTrace();
            } else {
                msg = "unknown error！";
            }
        }
    }

    public void stopMqtt() {
        if (mMqttClient != null) {
            try {
                mMqttClient.disconnect();

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        isConnected = false;
    }


}
