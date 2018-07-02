package com.qtctek.realstate.view.new_post.images_information;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.interfaces.OnRequireHandleFromAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Uri> mArrImagesUri;
    private OnRequireHandleFromAdapter mHandleFromAdapter;

    public ImageAdapter(Context mContext, ArrayList<Uri> mArrImagesLink, OnRequireHandleFromAdapter handleFromAdapter) {
        this.mContext = mContext;
        this.mArrImagesUri = mArrImagesLink;
        this.mHandleFromAdapter = handleFromAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(mContext).inflate(R.layout.item_image_new_post, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try{
            String[] typeFile = this.mArrImagesUri.get(position).toString().split("_");

            int temp = Integer.parseInt(typeFile[0]);
            String url = MainActivity.HOST + "/real_estate/images/" + this.mArrImagesUri.get(position).toString();
            Picasso.with(mContext).load(url).into(holder.imvImage);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandleFromAdapter.onRequireDeleteFile(position);
                }
            });

        }
        catch(java.lang.NumberFormatException e){
            holder.imvImage.setImageURI(this.mArrImagesUri.get(position));
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagesInformationFragment.ARR_URI.remove(position);
                    ImagesInformationFragment.QUALITY_IMAGE -= 1;
                    ImagesInformationFragment.IMAGE_ADAPTER.notifyDataSetChanged();
                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return this.mArrImagesUri.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imvImage;
        Button btnDelete;

        public ViewHolder(final View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imv_image);
            this.btnDelete = itemView.findViewById(R.id.btn_delete);


        }
    }

}
