package com.qtctek.realstate.view.user_control.user_management;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.dto.User_Object;

import java.util.ArrayList;

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

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = this.mArrLisUser.get(position);
        if(user == null){
            return null;
        }
        viewHolder.txvName.setText("Tên: " + user.getFullName());
        viewHolder.txvNumberPhone.setText("SDT: " + user.getPhone());
        viewHolder.txvEmail.setText("Email: " + user.getEmail());
        viewHolder.txvRole.setText(user.getType());

        if(user.getLevel() == 1){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorAdmin));
        }
        else if(user.getLevel() == 3){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorMonitor));
        }
        else if(user.getLevel() == 2){
            viewHolder.txvRole.setTextColor(mContext.getResources().getColor(R.color.colorUser));
        }

        if(mArrLisUser.get(position).getStatus().equals("no")){
            viewHolder.rlItemUser.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostNotActive));
        }
        else{
            viewHolder.rlItemUser.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostActive));
        }

        return convertView;

    }

    class ViewHolder{
        TextView txvName, txvNumberPhone, txvEmail, txvRole;
        RelativeLayout rlItemUser;
    }

}
