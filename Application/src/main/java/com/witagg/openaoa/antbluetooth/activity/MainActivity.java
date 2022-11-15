
//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.witagg.openaoa.antbluetooth.adpter.MainPagerAdapter;
import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.Fragment.BluetoothFragment;
import com.witagg.openaoa.antbluetooth.Fragment.MqttFragment;
import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;
import com.witagg.openaoa.antbluetooth.customerview.ChildViewPager;


public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    public MainPagerAdapter mPageAdapter;
    private ChildViewPager mViewPager;
    private long mLastClickTime = 0;
    public int tabPosition = 0;
    public boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        mContext = this;
        mPageAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mPageAdapter.init();
        mViewPager = (ChildViewPager) findViewById(R.id.viewpager);
        mViewPager.setSlide(false);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mPageAdapter.getTabView(i));

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


    }





    public Fragment getFragment(int postion) {
        if(postion < 0 || postion > mPageAdapter.getCount() - 1) {
            return  null;
        }
        Fragment fragment =  mPageAdapter.getItem(postion);
        return fragment;
    }

    public void startEnterBackgroud() {
        BluetoothFragment bluetoothFragment = (BluetoothFragment) getFragment(0);
        if (bluetoothFragment instanceof BluetoothFragment) {
            bluetoothFragment.stopProcess();
        }

        MqttFragment mqttFragment = (MqttFragment) getFragment(1);
        if (mqttFragment instanceof MqttFragment) {
            mqttFragment.stopProcess();
        }
    }

}
