package com.qtctek.realstate.view.user_control.post_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterPostSale extends BaseAdapter {

    private ArrayList<Product> mArrProduct;
    private Context mContext;


    public AdapterPostSale(ArrayList<Product> mArrProduct, Context mContext) {
        this.mArrProduct = mArrProduct;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mArrProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mArrProduct.get(position);
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
            viewHolder.rlItem = convertView.findViewById(R.id.rl_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imv_product_avartar);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);
            viewHolder.txvNamePoster = convertView.findViewById(R.id.txv_name_poster);
            viewHolder.txvPhoneNumberPoster = convertView.findViewById(R.id.txv_phone_number_poster);
            viewHolder.txvEmailPoster = convertView.findViewById(R.id.txv_email_poster);
            viewHolder.txvPostDate = convertView.findViewById(R.id.txv_post_date);
            viewHolder.progressBar = convertView.findViewById(R.id.progress_bar);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = this.mArrProduct.get(position);
        if(product == null){
            return null;
        }

        viewHolder.txvArea.setText(product.getArea() + " m²");
        viewHolder.txvDistrictProvinceCity.setText(product.getDistrict() + ", "
                + product.getCity());
        viewHolder.txvNamePoster.setText("Người đăng: " + product.getUserFullName());
        viewHolder.txvPhoneNumberPoster.setText("SDT:" + product.getUserPhone());
        viewHolder.txvEmailPoster.setText("Email: " + product.getUserPhone());

        viewHolder.txvPostDate.setText("Ngày đăng: " + formatDate(product.getDateUpload()));

        String urlImage = MainActivity.WEB_SERVER + "images/" + product.getThumbnail();
        final ViewHolder finalViewHolder = viewHolder;
        Picasso.with(mContext).load(urlImage).into(viewHolder.imvProductAvartar, new Callback() {
            @Override
            public void onSuccess() {
                finalViewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                finalViewHolder.progressBar.setVisibility(View.GONE);
                finalViewHolder.imvProductAvartar.setBackgroundResource(R.drawable.icon_product);
            }
        });

        String strPrice = "Thương lượng";
        if(product.getPrice() > 1000000000){
            float price = (float)product.getPrice() / 1000000000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + " tỉ";
            viewHolder.txvPrice.setText(strPrice);
        }
        else if(product.getPrice() > 1000000){
            float price = (float)product.getPrice() / 1000000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + " triệu";
            viewHolder.txvPrice.setText(strPrice);
        }
        else if(product.getPrice() > 1000){
            float price = (float)product.getPrice() / 1000;

            strPrice  = Math.round( price * 100.0)/ 100.0 + "K";
            viewHolder.txvPrice.setText(strPrice);
        }
        else{
            viewHolder.txvPrice.setText(strPrice);
        }

        if(product.getStatus().equals("2")){
            viewHolder.rlItem.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostNotActive));
        }
        else if(product.getStatus().equals("3")){
            viewHolder.rlItem.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPostActive));
        }
        else{
            viewHolder.rlItem.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorMainBackground));
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
        RelativeLayout rlItem;
        ImageView imvProductAvartar;
        TextView txvPrice, txvArea, txvDistrictProvinceCity, txvPostDate, txvNamePoster,
        txvPhoneNumberPoster, txvEmailPoster;
        ProgressBar progressBar;
    }

}
