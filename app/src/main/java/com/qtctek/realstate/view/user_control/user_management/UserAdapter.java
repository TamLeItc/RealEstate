package com.qtctek.realstate.view.user_control.user_management;

import android.content.Context;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserAdapter extends BaseAdapter {

    private ArrayList<User> mArrLisUser;
    private Context mContext;

    public UserAdapter(ArrayList<User> mArrLisUser, Context mContext) {
        this.mArrLisUser = mArrLisUser;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mArrLisUser.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrLisUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txvName = convertView.findViewById(R.id.txv_name);
            viewHolder.txvEmail = convertView.findViewById(R.id.txv_email_address);
            viewHolder.txvNumberPhone = convertView.findViewById(R.id.txv_phone_number);
            viewHolder.txvRole = convertView.findViewById(R.id.txv_role);
            viewHolder.rlItemUser = convertView.findViewById(R.id.rl_item_user);
            viewHolder.txvStatus = convertView.findViewById(R.id.txv_status);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = this.mArrLisUser.get(position);
        if(user == null){
            return null;
        }
        viewHolder.txvName.setText(mContext.getResources().getString(R.string.name) + ": " + user.getFullName());
        viewHolder.txvNumberPhone.setText(mContext.getResources().getString(R.string.acronym_number_phone) + "SDT: " + user.getPhone());
        viewHolder.txvEmail.setText("Email: " + user.getEmail());
        viewHolder.txvRole.setText(user.getType());

        if(user.getLevel() == 1){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorAdmin));
        }
        else if(user.getLevel() == 2){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorMonitor));
        }
        else if(user.getLevel() == 3){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorUser));
        }

        if(mArrLisUser.get(position).getStatus().equals(Constant.NO)){
            viewHolder.txvStatus.setText(mContext.getResources().getString(R.string.not_active));
            viewHolder.txvStatus.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        }
        else{
            viewHolder.txvStatus.setText(mContext.getResources().getString(R.string.active));
            viewHolder.txvStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }

        return convertView;

    }

    class ViewHolder{
        TextView txvName, txvNumberPhone, txvEmail, txvRole, txvStatus;
        RelativeLayout rlItemUser;
    }

}
