//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//
package com.witagg.openaoa.antbluetooth.adpter;

import java.util.ArrayList;

//{"recv_time": "2022-02-10 10:31:05", "mac": "B1:06:88:51:02:01",
// "seq": 297001, "rssi": -81, "freq": 2480000000, "aoa_2d": [55, 278]}
public class DeviceInfo {
    private String mac;
    private String recv_time;
    private long seq;
    private long freq;
    private long rssi;
    private String angel_1;
    private String angel_2;
    private float aoa_2d[];
    public String getMac() {
        return mac;
    }

    public void setMac(String dev_mac) {
        this.mac = dev_mac;
    }

    public String getAngel_1() {
        return angel_1;
    }

    public void setAngel_1(String angel_1) {
        this.angel_1 = angel_1;
    }

    public String getAngel_2() {
        return angel_2;
    }

    public void setAngel_2(String angel_2) {
        this.angel_2 = angel_2;
    }

    public void setFreq(long freq) {
        this.freq = freq;
    }

    public long getFreq() {
        return freq;
    }

    public void setRecv_time(String recv_time) {
        this.recv_time = recv_time;
    }

    public String getRecv_time() {
        return recv_time;
    }

    public void setRssi(long rssi) {
        this.rssi = rssi;
    }

    public long getRssi() {
        return rssi;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getSeq() {
        return seq;
    }

    public void setAoa_2d(float[] aoa_2d) {
        this.aoa_2d = aoa_2d;
        this.setAngel_1(String.valueOf(getAoa_2d()[1]));
        this.setAngel_2(String.valueOf(getAoa_2d()[0]));
    }

    public float[] getAoa_2d() {
        return aoa_2d;
    }
}
