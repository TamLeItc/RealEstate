package com.qtctek.aladin.view.user_control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.aladin.R;

import java.util.ArrayList;

public class UserFilterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mArrList;
    private int mOption;

    public UserFilterAdapter(Context mContext, ArrayList<String> mArrList, int option) {
        this.mContext = mContext;
        this.mArrList = mArrList;
        this.mOption = option;
    }

    @Override
    public int getCount() {
        return this.mArrList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_1, parent, false);

        TextView txvItem = convertView.findViewById(R.id.txv_item);
        ImageView imvSelected = convertView.findViewById(R.id.imv_selected);
        View view = convertView.findViewById(R.id.view_boundary);

        txvItem.setText(this.mArrList.get(position));
        if(mOption == position){
            imvSelected.setVisibility(View.VISIBLE);
        }
        else{
            imvSelected.setVisibility(View.GONE);
        }

        if(position == mArrList.size() - 1){
            view.setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
