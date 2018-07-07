package com.qtctek.realstate.view.post_news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter{

    private ArrayList<PostSale> mArrListPost;
    private Context mContext;

    public PostAdapter(Context context, ArrayList<PostSale> mArrListProduct) {
        this.mArrListPost = mArrListProduct;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.mArrListPost.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrListPost.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_post_2, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llItem = convertView.findViewById(R.id.ll_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imv_product_avartar);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvRoomsArea = convertView.findViewById(R.id.txv_rooms_area);
            viewHolder.txvAddress = convertView.findViewById(R.id.txv_address);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);
            viewHolder.progressbar = convertView.findViewById(R.id.progress_bar);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostSale postSale = this.mArrListPost.get(position);

        String area = postSale.getProduct().getArea() + " m²";
        String roomsArea = postSale.getProduct().getBedrooms() + " phòng ngủ, " + postSale.getProduct().getBathrooms() + " phòng tắm, " + area;
        viewHolder.txvRoomsArea.setText(roomsArea);

        viewHolder.txvAddress.setText(postSale.getProduct().getAddress());

        String districtProvinceCity = postSale.getProduct().getDistrict() + ", " + postSale.getProduct().getProvinceCity();
        viewHolder.txvDistrictProvinceCity.setText(districtProvinceCity);

        String urlImage = MainActivity.HOST + "/real_estate/images/" +postSale.getId() + "_avartar.jpg";
        final ViewHolder finalViewHolder = viewHolder;
        Picasso.with(mContext).load(urlImage).into(viewHolder.imvProductAvartar, new Callback() {
            @Override
            public void onSuccess() {
                finalViewHolder.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                finalViewHolder.progressbar.setVisibility(View.GONE);
            }
        });

        String strPrice = "Thương lượng";
        if(postSale.getProduct().getPrice() > 1000000000){
            float price = (float)postSale.getProduct().getPrice() / 1000000000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + " tỉ";
            viewHolder.txvPrice.setText(strPrice);
        }
        else if(postSale.getProduct().getPrice() > 1000000){
            float price = (float)postSale.getProduct().getPrice() / 1000000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + " triệu";
            viewHolder.txvPrice.setText(strPrice);
        }
        else if(postSale.getProduct().getPrice() > 1000){
            float price = (float)postSale.getProduct().getPrice() / 1000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + "K";
            viewHolder.txvPrice.setText(strPrice);
        }
        else{
            viewHolder.txvPrice.setText(strPrice);
        }
        return convertView;

    }

    class ViewHolder{
        LinearLayout llItem;
        ImageView imvProductAvartar;
        TextView txvPrice, txvRoomsArea, txvAddress, txvDistrictProvinceCity;
        ProgressBar progressbar;
    }

}
