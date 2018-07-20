package com.qtctek.realstate.view.post_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.user_control.saved_post.ViewHandleSavedPost;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.qtctek.realstate.view.post_news.activity.MainActivity.USER;

public class PostAdapter extends BaseAdapter implements ViewHandleSavedPost {

    private ArrayList<Product> mArrListProduct;
    private MapPostNewsFragment mMapPostNewsFragment;
    private Context mContext;
    private int mPositionClick;
    private ImageButton mImbSave;

    public PostAdapter(Context context, ArrayList<Product> mArrListProduct, MapPostNewsFragment mapPostNewsFragment) {
        this.mArrListProduct = mArrListProduct;
        this.mContext = context;
        this.mMapPostNewsFragment = mapPostNewsFragment;
    }

    @Override
    public int getCount() {
        return this.mArrListProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrListProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_post_2, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.rlItem = convertView.findViewById(R.id.rl_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imb_product_avartar);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvBathroom = convertView.findViewById(R.id.txv_bathroom);
            viewHolder.txvBedroom = convertView.findViewById(R.id.txv_bedroom);
            viewHolder.txvAddress = convertView.findViewById(R.id.txv_address);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);
            viewHolder.progressbar = convertView.findViewById(R.id.progress_bar);
            viewHolder.imbSave = convertView.findViewById(R.id.imb_save);
            viewHolder.txvAMonth = convertView.findViewById(R.id.txv_a_month);
            viewHolder.txvTitle = convertView.findViewById(R.id.txv_title);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = this.mArrListProduct.get(position);
        setValue(viewHolder, product, position);


        viewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("product_id", mArrListProduct.get(position).getId());
                intent.putExtra("save", mArrListProduct.get(position).getIsSaved());
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.imbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER.getLevel() != 3 && MainActivity.USER.getLevel() != User.USER_NULL){
                    MainActivity mainActivity = (MainActivity) mContext;
                    mainActivity.toastHelper.toast("User của bạn không thể sử dụng chức năng này", ToastHelper.LENGTH_SHORT);
                }
                else{
                    mPositionClick = position;
                    mImbSave = finalViewHolder1.imbSave;
                    handleSave();
                }
            }
        });

        return convertView;

    }

    private void setValue(ViewHolder viewHolder, Product product, final int position){
        viewHolder.txvTitle.setText(product.getTitle());
        viewHolder.txvArea.setText(product.getArea() + "");
        viewHolder.txvBedroom.setText(product.getBedroom() + "");
        viewHolder.txvBathroom.setText(product.getBathroom() + "");
        viewHolder.txvAddress.setText(product.getAddress() + "");

        String districtProvinceCity =product.getDistrict() + ", " + product.getCity();
        viewHolder.txvDistrictProvinceCity.setText(districtProvinceCity);

        String urlImage = MainActivity.WEB_SERVER + "images/" + product.getThumbnail();
        final ViewHolder finalViewHolder = viewHolder;
        Picasso.with(mContext).load(urlImage).into(viewHolder.imvProductAvartar, new Callback() {
            @Override
            public void onSuccess() {
                finalViewHolder.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                finalViewHolder.progressbar.setVisibility(View.GONE);
                finalViewHolder.imvProductAvartar.setImageResource(R.drawable.icon_product);
            }
        });

        viewHolder.txvPrice.setText(AppUtils.getStringPrice(product.getPrice(), AppUtils.LONG_PRICE));
        if(product.getIsSaved()){
            viewHolder.imbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
        }
        else{
            viewHolder.imbSave.setImageResource(R.drawable.icon_favorite_border_white_24dp);
        }

        if(product.getFormality().equals("no")){
            viewHolder.txvAMonth.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.txvAMonth.setVisibility(View.GONE);
        }
    }

    private void handleSave(){
        if(mArrListProduct.get(mPositionClick).getIsSaved()){
            try {
                ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.remove(mArrListProduct.get(mPositionClick).getId() + "");
            }
            catch (Exception e){}
        }
        else{
            ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.put(mArrListProduct.get(mPositionClick).getId() + "", mArrListProduct.get(mPositionClick).getId() + "");
        }
        new PresenterSavedPost(this).handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, mContext);
    }

    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {

    }

    @Override
    public void onHandleDataProductIdsError(String error) {

    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {
        if(this.mArrListProduct.get(mPositionClick).getIsSaved()){
            mMapPostNewsFragment.arrProduct.get(mPositionClick).setIsSaved(false);
            this.mImbSave.setImageResource(R.drawable.icon_favorite_border_white_24dp);
        }
        else{
            this.mImbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
            mMapPostNewsFragment.arrProduct.get(mPositionClick).setIsSaved(true);
        }
    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {

    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct) {

    }

    @Override
    public void onHandleSavedProductListError(String error) {

    }

    class ViewHolder{
        RelativeLayout rlItem;
        ImageView imvProductAvartar;
        ImageButton imbSave;
        TextView txvPrice, txvBedroom, txvBathroom, txvArea, txvAddress, txvDistrictProvinceCity, txvAMonth, txvTitle;
        ProgressBar progressbar;
    }



}


