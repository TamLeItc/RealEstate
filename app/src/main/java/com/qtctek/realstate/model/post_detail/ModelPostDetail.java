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

    private String mUrlProductDetail = MainActivity.HOST + "/real_estate/post_detail.php";

    private PresenterImpHandlePostDetail mPresenterImpHandlePostDetail;

    public ModelPostDetail(PresenterImpHandlePostDetail presenterImpHandlePostDetail){
        this.mPresenterImpHandlePostDetail = presenterImpHandlePostDetail;
    }

    public void requireGetProductDetail(int id){
        new ProductDetail(id).execute(mUrlProductDetail);
    }

    public void requireInsertSavePost(int postSaleId, String emailUser){
        new SaveProduct(postSaleId, emailUser).execute("");
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

            url = mUrlProductDetail + "?post_id=" + id;
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

    class SaveProduct extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int postSaleId;
        String emailUser;

        public SaveProduct(int postSaleId, String emailUser){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.postSaleId = postSaleId;
            this.emailUser = emailUser;
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlSave = MainActivity.HOST + "/real_estate/save_post.php?post_sale_id=" + postSaleId
                    + "&email_user=" + emailUser;

            Request request = new Request.Builder()
                    .url(urlSave)
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
            Log.d("ttt", s);
            mPresenterImpHandlePostDetail.onInsertDataSavePost(s);

            super.onPostExecute(s);
        }
    }

}
