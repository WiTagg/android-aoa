//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//

package com.witagg.openaoa.antbluetooth.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.witagg.openaoa.antbluetooth.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    private static final int TYPE_HEADER = 0; //titile
    private static final int TYPE_ITEM = 1; //item

    private List<DeviceType> mList;
    private LayoutInflater inflater;
    private Context mContext;

    public DeviceListAdapter(Context context, List<DeviceType> list){
        mContext = context;
        mList = list ;

    }
    @Override
    public int getCount() {
        int count = 0;
        if (mList != null) {
            for (DeviceType type : mList) {
                count += type.size();
            }
        }
        return  count;
    }
    @Override
    public Object getItem (int postion) {
        int head = 0;
        for (DeviceType type : mList) {
            int size = type.size();
            int current = postion - head;
            if (current < size) {
                return type.getItem(current);
            }
            head += size;
        }
        return null;
    }
    @Override
    public long getItemId(int postion) {
        return postion;
    }

    /**
     *
     * @return  item type count
     */

    public int getViewTypeCount() {
        return  2;
    }

    /**
     * get current item type
     * @param position
     * @return
     */

    public int getItemViewType(int position) {
        int head = 0;
        for (DeviceType type : mList) {
            int size = type.size();
            int current = position - head;
            if (current == 0) {
                return TYPE_HEADER;
            }
            head += size;
        }
        return TYPE_ITEM;
    }
    // convertView : cache had loaded layout
    @Override
    public View getView(int position, View convertView, ViewGroup parent){


        ViewHolder viewHolder;
        switch (getItemViewType(position)) {
            case TYPE_HEADER://titile layout
                if (convertView == null){
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_head, parent, false);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.device_head);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                String str_title = (String) getItem(position);
                viewHolder.title.setText(str_title);
                break;
            case TYPE_ITEM://item layout
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
                    viewHolder.deviceMac = (TextView) convertView.findViewById(R.id.device_mac);
                    viewHolder.deviceAngel1 = (TextView) convertView.findViewById(R.id.device_angel1);
                    viewHolder.deviceAngel2 = (TextView) convertView.findViewById(R.id.device_angel2);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                DeviceInfo device =(DeviceInfo) getItem(position);
                viewHolder.deviceMac.setText(device.getMac());
                viewHolder.deviceAngel1.setText(device.getAngel_1());
                viewHolder.deviceAngel2.setText(device.getAngel_2());
        }
        return convertView;
    }

    public void setDatas(List<DeviceType> list) {
        if (list != null) {
            if(this.mList == null) {
                this.mList = new ArrayList<DeviceType>() ;
            }
            this.mList = list;

        } else {
            if (this.mList != null)
                this.mList.clear();
        }
        this.notifyDataSetChanged();
    }

    //
    class ViewHolder{
        TextView title;
        TextView deviceMac;
        TextView deviceAngel1;
        TextView deviceAngel2;
    }
}
