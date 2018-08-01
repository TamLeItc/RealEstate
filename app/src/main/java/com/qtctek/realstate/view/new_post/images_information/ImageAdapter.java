package com.qtctek.realstate.view.new_post.images_information;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Photo photo = mArrPhoto.get(position);
        if (!photo.getIsUpload()) {
            String url = MainActivity.WEB_SERVER + MainActivity.IMAGE_URL_RELATIVE + this.mArrPhoto.get(position).getPhotoLink();
            Log.d("ttt", url);
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

        holder.rlImageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostActivity newPostActivity = (NewPostActivity)mContext;

                ImagesInformationFragment imagesInformationFragment;
                try {
                    imagesInformationFragment = (ImagesInformationFragment) newPostActivity.getSupportFragmentManager()
                            .getFragments().get(0);
                }
                catch (Exception e){
                    imagesInformationFragment = (ImagesInformationFragment) newPostActivity.getSupportFragmentManager()
                            .getFragments().get(1);
                }

                imagesInformationFragment.imvViewLarge.setImageDrawable(holder.imvImage.getDrawable());

                Animation animationUtils = AnimationUtils.loadAnimation(mContext, R.anim.zoom_0_to_100);
                imagesInformationFragment.rlViewLarge.startAnimation(animationUtils);
                imagesInformationFragment.rlViewLarge.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.mArrPhoto.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imvImage;
        Button btnDelete;
        RelativeLayout rlImageDetail;

        public ViewHolder(final View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imv_image);
            this.btnDelete = itemView.findViewById(R.id.btn_delete);
            this.rlImageDetail = itemView.findViewById(R.id.rl_image_detail);
        }
    }

}
