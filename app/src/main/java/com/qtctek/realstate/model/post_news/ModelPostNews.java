package com.qtctek.realstate.model.post_news;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.qtctek.realstate.presenter.post_news.PresenterImpHandlePostNews;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelPostNews {

    private String mUrlGetPostList = MainActivity.HOST + "/real_estate/get_post_list.php";
    private PresenterImpHandlePostNews mPresenterImpHandlePostNews;



    public ModelPostNews( PresenterImpHandlePostNews mPresenterImpHandlePostNews){
        this.mPresenterImpHandlePostNews = mPresenterImpHandlePostNews;
    }

    public void requireGetPostList(String minPrice, String maxPrice, String categoryProduct,
                                   LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){
        new GetPostListWithAddress( minPrice, maxPrice, categoryProduct,  farRight, nearRight, farLeft, nearLeft)
            .execute(mUrlGetPostList);
    }


    class GetPostListWithAddress extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient;

        String minPrice, maxPrice, categoryProduct;
        LatLng farRight;
        LatLng nearRight;
        LatLng farLeft;
        LatLng nearLeft;


        public GetPostListWithAddress(String minPrice, String maxPrice, String categoryProduct,
                                     LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.farRight = farRight;
            this.nearLeft = nearLeft;
            this.farLeft = farLeft;
            this.nearRight = nearRight;
            this.categoryProduct = categoryProduct;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("far_right_lat", farRight.latitude + "")
                    .addFormDataPart("far_left_lat", farLeft.latitude + "")
                    .addFormDataPart("near_right_lat", nearRight.latitude + "")
                    .addFormDataPart("near_left_lat", nearLeft.latitude + "")
                    .addFormDataPart("far_right_lng", farRight.longitude + "")
                    .addFormDataPart( "far_left_lng", farLeft.longitude + "")
                    .addFormDataPart("min_prices", minPrice)
                    .addFormDataPart("max_prices", maxPrice)
                    .addFormDataPart("category_product", categoryProduct)
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("error")){
                mPresenterImpHandlePostNews.getPostListSuccessful(s);
            }
            else{
                mPresenterImpHandlePostNews.getPostListError(s);
            }

            super.onPostExecute(s);
        }
    }


}
