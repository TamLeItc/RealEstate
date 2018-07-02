package com.qtctek.realstate.view.user_control.post_management;

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
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterPostSale extends BaseAdapter {

    private ArrayList<PostSale> mArrListPostSale;
    private Context mContext;


    public AdapterPostSale(ArrayList<PostSale> mArrListPostSale, Context mContext) {
        this.mArrListPostSale = mArrListPostSale;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mArrListPostSale.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrListPostSale.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_post_for_admin, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llItem = convertView.findViewById(R.id.ll_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imv_product_avartar);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);
            viewHolder.txvNamePoster = convertView.findViewById(R.id.txv_name_poster);
            viewHolder.txvPhoneNumberPoster = convertView.findViewById(R.id.txv_phone_number_poster);
            viewHolder.txvEmailPoster = convertView.findViewById(R.id.txv_email_poster);
            viewHolder.txvPostDate = convertView.findViewById(R.id.txv_post_date);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostSale postSale = this.mArrListPostSale.get(position);

        viewHolder.txvArea.setText(postSale.getProduct().getArea() + " m²");
        viewHolder.txvDistrictProvinceCity.setText(postSale.getProduct().getDistrict() + ", "
                + postSale.getProduct().getProvinceCity());
        viewHolder.txvNamePoster.setText("Người đăng: " + postSale.getUser().getName());
        viewHolder.txvPhoneNumberPoster.setText("SDT:" + postSale.getUser().getPhoneNumber());
        viewHolder.txvEmailPoster.setText("Email: " + postSale.getUser().getEmail());

        viewHolder.txvPostDate.setText("Ngày đăng: " + formatDate(postSale.getPostDate()));

        String urlImage = MainActivity.HOST + "/real_estate/images/" +postSale.getId() + "_avartar.jpg";
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

    private String formatDate(String dt){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date = simpleDateFormat.parse(dt);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            return df.format(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "";
    }

    class ViewHolder{
        LinearLayout llItem;
        ImageView imvProductAvartar;
        TextView txvPrice, txvArea, txvDistrictProvinceCity, txvPostDate, txvNamePoster,
        txvPhoneNumberPoster, txvEmailPoster;
    }

}
