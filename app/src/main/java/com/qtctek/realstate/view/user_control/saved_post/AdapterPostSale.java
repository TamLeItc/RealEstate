package com.qtctek.realstate.view.user_control.saved_post;

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
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPostSale extends BaseAdapter {

    private ArrayList<Product> mArrProduct;
    private Context mContext;
    private int mLayoutAdapter;

    public AdapterPostSale(ArrayList<Product> mArrListProduct, Context context, int layoutAdapter) {
        this.mArrProduct = mArrListProduct;
        this.mContext = context;
        this.mLayoutAdapter = layoutAdapter;
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

            convertView = LayoutInflater.from(mContext).inflate(mLayoutAdapter, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llItem = convertView.findViewById(R.id.ll_item);
            viewHolder.imvProductAvartar = convertView.findViewById(R.id.imb_product_avartar);
            viewHolder.txvArea = convertView.findViewById(R.id.txv_area);
            viewHolder.txvPrice = convertView.findViewById(R.id.txv_price);
            viewHolder.txvRooms = convertView.findViewById(R.id.txv_rooms);
            viewHolder.txvTitle = convertView.findViewById(R.id.txv_title);
            viewHolder.txvAddress = convertView.findViewById(R.id.txv_address);
            viewHolder.progressBar = convertView.findViewById(R.id.progress_bar);
            viewHolder.txvAMonth = convertView.findViewById(R.id.txv_a_month);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = this.mArrProduct.get(position);

        viewHolder.txvArea.setText(product.getArea() + " m²");
        viewHolder.txvRooms.setText(product.getBedroom() + " " + mContext.getResources().getString(R.string.bedroom) +", "
                + product.getBathroom() + " " + mContext.getResources().getString(R.string.bathroom));
        viewHolder.txvTitle.setText(product.getTitle());
        viewHolder.txvAddress.setText(product.getDistrict() + ", "
                + product.getCity());

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

        if(product.getFormality().equals("no")){
            viewHolder.txvAMonth.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.txvAMonth.setVisibility(View.GONE);
        }

        return convertView;

    }

    class ViewHolder{
        LinearLayout llItem;
        ImageView imvProductAvartar;
        TextView txvPrice, txvArea, txvRooms, txvTitle, txvAddress, txvAMonth;
        ProgressBar progressBar;
    }

}
