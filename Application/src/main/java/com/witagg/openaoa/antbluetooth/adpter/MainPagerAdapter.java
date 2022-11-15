//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//

package com.witagg.openaoa.antbluetooth.adpter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.Fragment.BluetoothFragment;
import com.witagg.openaoa.antbluetooth.Fragment.MqttFragment;


import java.util.ArrayList;
import java.util.List;


public class MainPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public void init() {
        this.addFragment(new BluetoothFragment(), mContext.getResources().getString(R.string.home_menu1));
        this.addFragment(new MqttFragment(), mContext.getResources().getString(R.string.home_menu2));

    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_textView);
        tv.setText(getPageTitle(position));
        Resources resources = mContext.getResources();
        ColorStateList csl = resources.getColorStateList(R.drawable.selector_item);
        if (csl != null) {
            tv.setTextColor(csl);
        }
        ImageView img = (ImageView) view.findViewById(R.id.tab_imageView);
        img.setImageResource(imageResId[position]);
        if (position == 0) {
            //view.setSelected(true);
        }
        return view;
    }


    private int[] imageResId = {R.drawable.tab_bluetooth,
            R.drawable.tab_mqtt_list
            };

}
