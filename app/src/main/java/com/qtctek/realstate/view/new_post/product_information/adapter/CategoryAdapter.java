package com.qtctek.realstate.view.new_post.product_information.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Category> mArr;
    private int mPositionSelected;

    public CategoryAdapter(Context mContext, ArrayList<Category> mArr, int positionSelected) {
        this.mContext = mContext;
        this.mArr = mArr;
        mPositionSelected = positionSelected;
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

        if(this.mPositionSelected - 1 == position){
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
