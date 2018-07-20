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
import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.interfaces.OnRequireHandleFromAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Photo> mArrPhoto;
    private OnRequireHandleFromAdapter mHandleFromAdapter;

    public ImageAdapter(Context mContext, ArrayList<Photo> mArrPhoto, OnRequireHandleFromAdapter handleFromAdapter) {
        this.mContext = mContext;
        this.mArrPhoto = mArrPhoto;
        this.mHandleFromAdapter = handleFromAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(mContext).inflate(R.layout.item_image_new_post, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Photo photo = mArrPhoto.get(position);
        if (!photo.getIsUpload()) {
            String url = MainActivity.WEB_SERVER + "/images/" + this.mArrPhoto.get(position).getPhotoLink();
            Picasso.with(mContext).load(url).into(holder.imvImage);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandleFromAdapter.onRequireDeleteFile(position);
                }
            });
        } else {
            holder.imvImage.setImageURI(photo.getUri());
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mArrPhoto.remove(position);
                    ImagesInformationFragment.IMAGE_ADAPTER.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.mArrPhoto.size();
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
