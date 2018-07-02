package com.qtctek.realstate.view.post_news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.CategoriesProduct;

import java.util.ArrayList;

public class CategoriesProductAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CategoriesProduct> mArr;

    public CategoriesProductAdapter(Context mContext, ArrayList<CategoriesProduct> mArr) {
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

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txvItem = convertView.findViewById(R.id.txv_item);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.txvItem.setText(this.mArr.get(position).getName());

        return convertView;
    }

    class ViewHolder{
        TextView txvItem;
    }

}
