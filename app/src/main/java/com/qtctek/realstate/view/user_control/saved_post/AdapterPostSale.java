package com.qtctek.realstate.view.user_control.saved_post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.qtctek.realstate.view.post_news.activity.MainActivity.HOST;

public class AdapterPostSale extends BaseAdapter {

    private ArrayList<PostSale> mArrListPost;
    private Context mContext;
    private int mLayoutAdapter;

    public AdapterPostSale(ArrayList<PostSale> mArrListProduct, Context context, int layoutAdapter) {
        this.mArrListPost = mArrListProduct;
        this.mContext = context;
        this.mLayoutAdapter = layoutAdapter;
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

            convertView = LayoutInflater.from(mContext).inflate(mLayoutAdapter, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llItem = convertView.findViewById(R.id.ll_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imv_product_avartar);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvRooms = convertView.findViewById(R.id.txv_rooms);
            viewHolder.txvAddress = convertView.findViewById(R.id.txv_address);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostSale postSale = this.mArrListPost.get(position);

        viewHolder.txvArea.setText(postSale.getProduct().getArea() + " m²");
        viewHolder.txvRooms.setText(postSale.getProduct().getBedrooms() + " phòng ngủ, " + postSale.getProduct().getBathrooms() + " phòng tắm");
        viewHolder.txvAddress.setText(postSale.getProduct().getAddress());
        viewHolder.txvDistrictProvinceCity.setText(postSale.getProduct().getDistrict() + ", " + postSale.getProduct().getProvinceCity());

        String urlImage = HOST + "/real_estate/images/" +postSale.getId() + "_avartar.jpg";
        Picasso.with(mContext).load(urlImage).into(viewHolder.imvProductAvartar);

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

        if(postSale.getStatus().equals("Chưa duyệt")){
            viewHolder.llItem.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostNotActive));
        }
        else if(postSale.getStatus().equals("Đã đăng")){
            viewHolder.llItem.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostActive));
        }

        return convertView;

    }

    class ViewHolder{
        LinearLayout llItem;
        ImageView imvProductAvartar;
        TextView txvPrice, txvArea, txvRooms, txvAddress, txvDistrictProvinceCity;
    }

}
