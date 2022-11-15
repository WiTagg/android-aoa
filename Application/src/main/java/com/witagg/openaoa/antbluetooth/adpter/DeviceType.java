//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//

package com.witagg.openaoa.antbluetooth.adpter;

import java.util.ArrayList;

public class DeviceType {
    private String title;
    private ArrayList<DeviceInfo> mList;
    public DeviceType(String titile) {
        this.title = titile;
        mList = new ArrayList<>();
    }

    public void addItem(DeviceInfo deviceInfo) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        Boolean res = false;
        for(int i = 0; i < mList.size(); i++) {
            DeviceInfo deviceInfo1 = mList.get(i);
            if (deviceInfo.getMac().equals(deviceInfo1.getMac())) {
                deviceInfo1.setAngel_2(deviceInfo.getAngel_2());
                deviceInfo1.setAngel_1(deviceInfo.getAngel_1());
                deviceInfo1.setFreq(deviceInfo.getFreq());
                deviceInfo1.setRssi(deviceInfo.getRssi());
                deviceInfo1.setRecv_time(deviceInfo.getRecv_time());
                deviceInfo1.setSeq(deviceInfo.getSeq());
                res = true;
                break;
            }
        }
        if(res == false) {
            mList.add(deviceInfo);
        }
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
        }
    }
    public Object getItem(int position) {
        if (position == 0) {
            return title;
        } else {
            return mList.get(position - 1);
        }
    }

    public int size() {
        return mList.size() + 1;
    }


}


