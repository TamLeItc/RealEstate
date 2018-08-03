package com.qtctek.aladin.view.user_control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.util.ArrayList;

public class PostFilterAdapter extends RecyclerView.Adapter<PostFilterAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mArrList;
    private int mStatusSelected;
    private StatusAdapterCallback mCallback;

    private ImageView mImvSelected;

    public PostFilterAdapter(Context mContext, ArrayList<String> mArrList, int mStatusSelected, StatusAdapterCallback callback) {
        this.mContext = mContext;
        this.mArrList = mArrList;
        this.mStatusSelected = mStatusSelected;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txvItem.setText(mArrList.get(position));

        if(position == mStatusSelected){
            holder.imvSelected.setVisibility(View.VISIBLE);
            mImvSelected = holder.imvSelected;
        }
        else{
            holder.imvSelected.setVisibility(View.GONE);
        }

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveTemp = mContext.getResources().getString(R.string.save_temp);
                if(MainActivity.USER.getLevel() != 3 && mArrList.get(position).equals(saveTemp)){
                    return;
                }

                mCallback.onItemSelected(position);

                mImvSelected.setVisibility(View.GONE);
                holder.imvSelected.setVisibility(View.VISIBLE);
                mImvSelected = holder.imvSelected;
            }
        });

        String saveTemp = mContext.getResources().getString(R.string.save_temp);
        if(MainActivity.USER.getLevel() != 3 && mArrList.get(position).equals(saveTemp)){
            holder.txvItem.setTextColor(mContext.getResources().getColor(R.color.colorGrayLight));
        }
        else{
            holder.txvItem.setTextColor(mContext.getResources().getColor(R.color.colorGrayDark));
        }

        if(position == mArrList.size() - 1){
            holder.viewScroll.setVisibility(View.GONE);
        }
        else{
            holder.viewScroll.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mArrList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rlItem;
        TextView txvItem;
        ImageView imvSelected;
        View viewScroll;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rlItem = itemView.findViewById(R.id.rl_item_1);
            this.txvItem = itemView.findViewById(R.id.txv_item);
            this.imvSelected = itemView.findViewById(R.id.imv_selected);
            this.viewScroll = itemView.findViewById(R.id.view_boundary);
        }
    }

    public interface StatusAdapterCallback{
        void onItemSelected(int position);
    }
}
