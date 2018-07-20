package com.qtctek.realstate.view.new_post.product_information.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Place;

import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Place> mArr;
    private int mPlaceIdSelected;

    public PlaceAdapter(Context mContext, ArrayList<Place> mArr, int placeIdSelected) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mPlaceIdSelected = placeIdSelected;
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

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txvItem = convertView.findViewById(R.id.txv_item);
            viewHolder.imvSelected = convertView.findViewById(R.id.imv_selected);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(this.mArr.get(position).getId() == mPlaceIdSelected){
            viewHolder.imvSelected.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.imvSelected.setVisibility(View.GONE);
        }

        viewHolder.txvItem.setText(this.mArr.get(position).getName());

        return convertView;
    }

    class ViewHolder{
        TextView txvItem;
        ImageView imvSelected;
    }

}
