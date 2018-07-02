package com.qtctek.realstate.view.post_detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mArrImage;

    public ImageAdapter(Context mContext, ArrayList<String> mArrImage) {
        this.mContext = mContext;
        this.mArrImage = mArrImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_post_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = MainActivity.HOST + "/real_estate/images/" + this.mArrImage.get(position);
        Picasso.with(mContext).load(url).into(holder.imvImage);
    }

    @Override
    public int getItemCount() {
        return this.mArrImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imvImage;


        public ViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imv_image);
        }
    }

}
