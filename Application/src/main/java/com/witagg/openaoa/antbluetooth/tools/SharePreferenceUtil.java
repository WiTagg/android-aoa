//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//

package com.witagg.openaoa.antbluetooth.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
public class SharePreferenceUtil {
    private SharedPreferences sp;
    private static SharedPreferences.Editor editor;


    public SharePreferenceUtil(Context context) {
        sp = context.getSharedPreferences(".sharepreference", context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }
    public void setMac(String value) {
        editor.putString("mac_str", value);
        editor.commit();
    }

    public String getMac() {
        String mac = "" ;
        return sp.getString("mac_str", mac);
    }

    public String getMqttIp() {
        String ipAddr = "" ;
        return sp.getString("mqtt_ip_str", ipAddr);
    }

    public void setMqttIp(String mqttIp) {
        editor.putString("mqtt_ip_str", mqttIp);
        editor.commit();
    }

    public String getMqttPort() {
        String portStr = "" ;
        return sp.getString("mqtt_port_str", portStr);
    }

    public void setMqttPort(String mqttPort) {
        editor.putString("mqtt_port_str", mqttPort);
        editor.commit();
    }

    public String getMqttTopic() {
        String topicStr = "" ;
        return sp.getString("mqtt_topic_str", topicStr);
    }

    public void setMqttTopic(String mqttTopic) {
        editor.putString("mqtt_topic_str", mqttTopic);
        editor.commit();
    }

    public void setKalmanStrength(int progress) {
        editor.putInt("kalman_strength", progress);
        editor.commit();
    }

    public int getKalmanStrength() {
        int progress = 6;
        return sp.getInt("kalman_strength", progress);
    }

    public void setTailLength(int progress) {
        editor.putInt("Tail_length", progress);
        editor.commit();
    }

    public int getTailLength() {
        int progress = 5;
        return sp.getInt("Tail_length", progress);
    }
}
