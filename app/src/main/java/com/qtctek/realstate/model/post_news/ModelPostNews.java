package com.qtctek.realstate.model.post_news;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.qtctek.realstate.common.AppUtils;
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

    private String mUrlGetPostList = MainActivity.WEB_SERVER + "get_list_product_with_location.php";
    private PresenterImpHandlePostNews mPresenterImpHandlePostNews;



    public ModelPostNews( PresenterImpHandlePostNews mPresenterImpHandlePostNews){
        this.mPresenterImpHandlePostNews = mPresenterImpHandlePostNews;
    }

    public void requireGetPostList(String option, int bedroom, int bathroom, String minPrice, String maxPrice, String formality, String architecture, String type,
                                   LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){
        new GetPostListWithAddress(option, bedroom, bathroom, minPrice, maxPrice, formality, architecture, type,  farRight, nearRight, farLeft, nearLeft)
            .execute(mUrlGetPostList);
    }


    class GetPostListWithAddress extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient;

        String option;
        int bedroom, bathroom;
        String minPrice, maxPrice, formality, architecture, type;
        LatLng farRight;
        LatLng nearRight;
        LatLng farLeft;
        LatLng nearLeft;


        public GetPostListWithAddress(String option, int bathroom, int bedroom, String minPrice, String maxPrice, String formality, String architecture,
                                      String type, LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){
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
            this.formality = formality;
            this.architecture = architecture;
            this.type = type;
            this.bedroom = bedroom;
            this.bathroom = bathroom;
            this.option = option;
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
                    .addFormDataPart("architecture", architecture)
                    .addFormDataPart("formality", formality)
                    .addFormDataPart("type", type)
                    .addFormDataPart(AppUtils.QUALITY_BATHROOM, bathroom + "")
                    .addFormDataPart(AppUtils.QUALITY_BEDROOM, bedroom + "")
                    .addFormDataPart("option", option)
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
                if(option.equals("count")){
                    mPresenterImpHandlePostNews.getQualityPostSuccessful(s);
                }
                else{
                    mPresenterImpHandlePostNews.getPostListSuccessful(s);
                }
            }
            else{
                if(option.equals("count")){
                    mPresenterImpHandlePostNews.getPostListError(s);
                }
                else{
                    mPresenterImpHandlePostNews.getQualityPostError(s);
                }
            }

            super.onPostExecute(s);
        }
    }


}
