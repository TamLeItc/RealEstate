package com.qtctek.realstate.view.post_news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Place;

import java.util.ArrayList;

public class DistrictAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Place> mArr;

    public DistrictAdapter(Context mContext, ArrayList<Place> mArr) {
        this.mContext = mContext;
        this.mArr = mArr;
    }

    @Override
    public int getCount() {
        return this.mArr.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DistrictAdapter.ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txvItem = convertView.findViewById(R.id.txv_item);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (DistrictAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txvItem.setText(this.mArr.get(position).getName());

        return convertView;
    }

    class ViewHolder{
        TextView txvItem;
    }

}
