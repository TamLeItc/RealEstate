package com.qtctek.aladin.view.user_control.saved_search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.dto.Condition;

import java.util.ArrayList;

public class SavedSearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Condition> mArrCondition;

    public SavedSearchAdapter(Context mContext, ArrayList<Condition> mArrCondition) {
        this.mContext = mContext;
        this.mArrCondition = mArrCondition;
    }

    @Override
    public int getCount() {
        return this.mArrCondition.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrCondition.get(position);
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
            viewHolder.txvNameSavedSearch = convertView.findViewById(R.id.txv_item);
            viewHolder.viewScroll = convertView.findViewById(R.id.view_boundary);
            viewHolder.rlItem1 = convertView.findViewById(R.id.rl_item_1);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txvNameSavedSearch.setText(this.mArrCondition.get(position).getName());

        viewHolder.rlItem1.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));

        viewHolder.viewScroll.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder{
        TextView txvNameSavedSearch;
        View viewScroll;
        RelativeLayout rlItem1;
    }

}
