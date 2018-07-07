package com.qtctek.realstate.model.post_detail;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.realstate.presenter.post_detail.PresenterImpHandlePostDetail;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModelPostDetail {

    private String mUrlProductDetail = MainActivity.WEB_SERVER + "get_product_detail.php";

    private PresenterImpHandlePostDetail mPresenterImpHandlePostDetail;

    public ModelPostDetail(PresenterImpHandlePostDetail presenterImpHandlePostDetail){
        this.mPresenterImpHandlePostDetail = presenterImpHandlePostDetail;
    }

    public void requireGetProductDetail(int productId){
        new ProductDetail(productId).execute(mUrlProductDetail);
    }

    class ProductDetail extends AsyncTask<String, Void, String>
    {
        OkHttpClient okHttpClient;

        String url = "";

        public ProductDetail(int id){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            url = mUrlProductDetail + "?product_id=" + id;
        }

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder()
                    .url(url)
                    .get()
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
                mPresenterImpHandlePostDetail.onGetDataPostDetailSuccessful(s);
            }
            else{
                mPresenterImpHandlePostDetail.onGetDataPostDetailError(s);
            }

            super.onPostExecute(s);
        }
    }
}
