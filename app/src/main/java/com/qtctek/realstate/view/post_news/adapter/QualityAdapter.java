package com.qtctek.realstate.view.post_news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.Room;
import com.qtctek.realstate.view.post_news.interfaces.OnFromAdapter;

import java.util.ArrayList;


public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.ViewHolder>{

    private Context mContext;
    private String mQualityCode;
    private OnFromAdapter mOnFromAdapter;
    private ArrayList<Room> mArrRoom;

    public QualityAdapter(Context mContext, ArrayList<Room> arrRoom, String qualityCode, OnFromAdapter onFromAdapter) {
        this.mContext = mContext;
        this.mQualityCode = qualityCode;
        this.mOnFromAdapter = onFromAdapter;
        this.mArrRoom = arrRoom;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_quality, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txvQuality.setText(mArrRoom.get(position).getQuality() + "");

        if(mArrRoom.get(position).isIsSelected()){
            holder.llItemQuality.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrayLight));
            holder.txvQuality.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.txvAdd.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        else{
            holder.llItemQuality.setBackgroundColor(mContext.getResources().getColor(R.color.colorDefaultBackground));
            holder.txvQuality.setTextColor(mContext.getResources().getColor(R.color.colorGrayDark));
            holder.txvAdd.setTextColor(mContext.getResources().getColor(R.color.colorGrayDark));
        }

        holder.llItemQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQualityCode.equals(AppUtils.QUALITY_BATHROOM)){
                    mOnFromAdapter.onItemBathroomClick(position);
                }
                else{
                    mOnFromAdapter.onItemBedroomClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return AppUtils.QUALITY_ELEMENT;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout llItemQuality;
        TextView txvQuality, txvAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txvQuality = itemView.findViewById(R.id.txv_quality);
            this.llItemQuality = itemView.findViewById(R.id.ll_item_quality);
            this.txvAdd = itemView.findViewById(R.id.txv_add);
        }
    }

}
