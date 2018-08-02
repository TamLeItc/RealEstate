package com.qtctek.realstate.view.user_control.post_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
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
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imb_product_avartar);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvDistrictProvinceCity = convertView.findViewById(R.id.txv_district_province_city);
            viewHolder.txvNamePoster = convertView.findViewById(R.id.txv_name_poster);
            viewHolder.txvPhoneNumberPoster = convertView.findViewById(R.id.txv_phone_number_poster);
            viewHolder.txvEmailPoster = convertView.findViewById(R.id.txv_email_poster);
            viewHolder.txvPostDate = convertView.findViewById(R.id.txv_post_date);
            viewHolder.progressBar = convertView.findViewById(R.id.progress_bar);
            viewHolder.txvAMonth = convertView.findViewById(R.id.txv_a_month);
            viewHolder.txvStatus = convertView.findViewById(R.id.txv_status);

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
        viewHolder.txvNamePoster.setText( mContext.getResources().getString(R.string.poster) + ": " + product.getUserFullName());
        viewHolder.txvPhoneNumberPoster.setText(mContext.getResources().getString(R.string.acronym_number_phone) + ":" + product.getUserPhone());
        viewHolder.txvEmailPoster.setText(mContext.getResources().getString(R.string.email) + ": " + product.getUserEmail());

        viewHolder.txvPostDate.setText(mContext.getResources().getString(R.string.upload_date) + ": " + formatDate(product.getDateUpload()));

        String urlImage = MainActivity.WEB_SERVER + MainActivity.IMAGE_URL_RELATIVE + product.getThumbnail();
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

        viewHolder.txvPrice.setText(AppUtils.getStringPrice(product.getPrice(), AppUtils.LONG_PRICE));

        if(product.getStatus().equals("1")){
            viewHolder.txvStatus.setText(mContext.getResources().getString(R.string.save_temp));
            viewHolder.txvStatus.setTextColor(mContext.getResources().getColor(R.color.colorGrayDark));
        }
        else if(product.getStatus().equals("2")){
            viewHolder.txvStatus.setText(mContext.getResources().getString(R.string.pending));
            viewHolder.txvStatus.setTextColor(mContext.getResources().getColor(R.color.colorRedLight));
        }
        else if(product.getStatus().equals("3")){
            viewHolder.txvStatus.setText(mContext.getResources().getString(R.string.posted));
            viewHolder.txvStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }

        if(product.getFormality().equals(Constant.NO)){
            viewHolder.txvAMonth.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.txvAMonth.setVisibility(View.GONE);
        }

        return convertView;
    }

    private String formatDate(String dt){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.YYYY_MM_DD);
            Date date = new Date();
            date = simpleDateFormat.parse(dt);

            SimpleDateFormat df = new SimpleDateFormat(Constant.DD_MM_YYYY);
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
        txvPhoneNumberPoster, txvEmailPoster, txvAMonth, txvStatus;
        ProgressBar progressBar;
    }

}
