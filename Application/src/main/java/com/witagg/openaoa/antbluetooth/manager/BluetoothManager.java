//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth.manager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;
import android.bluetooth.le.AdvertiseData;

import com.witagg.openaoa.antbluetooth.tools.BleUtil;

import static android.content.Context.BLUETOOTH_SERVICE;

public class BluetoothManager {

    public static final String TAG = "lock_mock_manager";

    private android.bluetooth.BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothAdvertiser;
    private Context mContext;

    private static class SingletonHolder {
        static final BluetoothManager instance = new BluetoothManager();
    }

    /**
     * Return the Acquisition singleton.
     */
    public synchronized static BluetoothManager getInstance() {
        return SingletonHolder.instance;
    }

    public BluetoothManager() {

    }

    public void setContext(Context context) {
        mContext = context;

        //init BluetoothManager and BluetoothAdapter
        if (mBluetoothManager == null) {
            mBluetoothManager = (android.bluetooth.BluetoothManager) mContext.getSystemService(BLUETOOTH_SERVICE);
        }

        if (mBluetoothManager != null && mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
    }

    public boolean init(Activity activity, int requestCode) {

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "Unsupported Bluetooth", Toast.LENGTH_LONG).show();
            //activity.finish();
            return false;
        }

        final android.bluetooth.BluetoothManager mBluetoothManager = (android.bluetooth.BluetoothManager) activity.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(activity, "Unsupported Bluetooth", Toast.LENGTH_LONG).show();
            //activity.finish();
            return false;
        }

        mBluetoothAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothAdvertiser == null) {
            Toast.makeText(activity, "Bluetooth is not turned on", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Bluetooth is not turned on");
            //activity.finish();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, requestCode);
            }
            return false;
        }



        return true;
    }


    public void startAdvertising(String mac,int channel) {

        //get BluetoothLeAdvertiser，This api is used to send ble
        if (mBluetoothAdvertiser == null) {
            mBluetoothAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        }
        if (mBluetoothAdvertiser != null) {
            try {
                /*
                The content of the broadcast package is fixed and cannot be modified
                * */
                byte[] switcherData = new byte[27];
                switcherData[0] = 0x04;
                byte[] macBytes = BleUtil.macStrToBytes(mac);
                System.arraycopy(macBytes,0,switcherData,1,macBytes.length);
                if(channel == 37) {
                   byte[] channelData =  {0x2f, 0x61, (byte)0xac, (byte)0xcc, 0x27, 0x45, 0x67,
                           (byte)0xf7, (byte)0xdb, 0x34, (byte)0xc4, 0x03, (byte)0x8e, 0x5c,
                           0x0b,(byte)0xaa, (byte)0x97, 0x30, 0x56, (byte)0xe6};
                   System.arraycopy(channelData,0,switcherData,1+macBytes.length,channelData.length);
                } else if(channel == 38) {
                    byte[] channelData = {(byte)0x6f, (byte)0xd3, (byte)0x10, (byte)0x0f, 0x38,
                            (byte)0x72, 0x2d, (byte)0xa8, (byte)0x5e, (byte)0xc2, (byte)0x58,
                            (byte)0x99, (byte)0x4f, (byte)0x8a, (byte)0xce,(byte)0xee,
                            (byte)0xb7, 0x69, (byte)0x88, (byte)0x07};
                    System.arraycopy(channelData,0,switcherData,1+macBytes.length,channelData.length);
                } else {
                    byte[] channelData = {0x50, (byte)0xbd, (byte)0x84, (byte)0xb1, 0x32, (byte)0x9f,
                            0x14, (byte)0x9d, (byte)0xdd, (byte)0x6f, (byte)0xd3, 0x10, (byte)0x0f,
                            0x38, 0x72,(byte)0x2d, (byte)0xa8, 0x5e, (byte)0xc2, (byte)0x58};
                    System.arraycopy(channelData,0,switcherData,1+macBytes.length,channelData.length);
                }
                AdvertiseSettings settings = new AdvertiseSettings.Builder()
                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                        .setTimeout(0)
                        .setConnectable(false)
                        .build();
                //create BLE beacon Advertising
                AdvertiseData advertiseData = new AdvertiseData.Builder()
                        .setIncludeDeviceName(false)
                        .setIncludeTxPowerLevel(false)
                        .addManufacturerData(0xd,switcherData)
                        //.addServiceData(new ParcelUuid(BluetoothUUID.bleServerUUID), switcherData)
                        //.addServiceUuid(new ParcelUuid(BluetoothUUID.bleServerUUID))
                        .build();
                mBluetoothAdvertiser.startAdvertising(settings,
                        advertiseData,mAdvCallback);
            } catch (Exception e) {
                Log.v(TAG, "Fail to setup BleService");
            }
        }
    }
    

    //send ble callback，onStartSuccess/onStartFailure
    private AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        public void onStartSuccess(android.bluetooth.le.AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Toast.makeText(mContext, "Advertise Start Success", Toast.LENGTH_SHORT).show();
            if (settingsInEffect != null) {
                Log.d(TAG, "onStartSuccess TxPowerLv=" + settingsInEffect.getTxPowerLevel() + " mode=" + settingsInEffect.getMode() + " timeout=" + settingsInEffect.getTimeout());
            } else {
                Log.d(TAG, "onStartSuccess, settingInEffect is null");
            }
        }

        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d(TAG, "onStartFailure errorCode=" + errorCode);

            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                Toast.makeText(mContext, "advertise_failed_data_too_large", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.");
            } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                Toast.makeText(mContext, "advertise_failed_too_many_advertises", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to start advertising because no advertising instance is available.");

            } else if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
                Toast.makeText(mContext, "advertise_failed_already_started", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to start advertising as the advertising is already started");

            } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
                Toast.makeText(mContext, "advertise_failed_internal_error", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Operation failed due to an internal error");

            } else if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
                Toast.makeText(mContext, "advertise_failed_feature_unsupported", Toast.LENGTH_LONG).show();
                Log.e(TAG, "This feature is not supported on this platform");

            }
        }
    };

    public void stopAdvertising() {
        //stop BluetoothLeAdvertiser，BluetoothAdapter，BluetoothGattServer
        if (mBluetoothAdvertiser != null) {
            mBluetoothAdvertiser.stopAdvertising(mAdvCallback);
            mBluetoothAdvertiser = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter = null;
        }
    }


}
