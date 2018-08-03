package com.qtctek.aladin.model.new_post;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.aladin.presenter.new_post.GetData.PresenterImpHandleModelGetData;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModelGetData {

    private PresenterImpHandleModelGetData mPresenterImpHandleModelGetData;

    String mUrlCity = MainActivity.WEB_SERVER + "get_city.php";
    String mUrlDistrict = MainActivity.WEB_SERVER + "get_district.php";
    String mUrlCategoriesProduct = MainActivity.WEB_SERVER + "get_category_product.php";

    public ModelGetData(PresenterImpHandleModelGetData presenterImpHandleModelGetData){
        this.mPresenterImpHandleModelGetData = presenterImpHandleModelGetData;
    }

    public void requireGetListCity(){
        new City().execute(mUrlCity);
    }

    public void requireGetListDistrict(int provinceCityId){
        new District(provinceCityId).execute(mUrlDistrict);
    }

    public void requireGetCategoriesProduct(String table, String columnName){
        new CategoryProduct(table, columnName).execute(mUrlCategoriesProduct);
    }

    class City extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        public City(){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
        }

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder()
                    .url(strings[0])
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
                mPresenterImpHandleModelGetData.onGetListCity(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetListCity(false, s);
            }

            super.onPostExecute(s);
        }
    }

    class District extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int cityId;

        public District(int cityId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.cityId = cityId;
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0] + "?city_id=" + cityId;
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
                mPresenterImpHandleModelGetData.onGetDistrict(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetDistrict(false, s);
            }

            super.onPostExecute(s);
        }
    }

    class CategoryProduct extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        private String table;
        private String columnName;

        public CategoryProduct(String table, String columnName){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.table = table;
            this.columnName = columnName;
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = mUrlCategoriesProduct + "?table=" + table + "&column_name=" + columnName;
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
                mPresenterImpHandleModelGetData.onGetCategoriesProduct(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetCategoriesProduct(false, s);
            }

            super.onPostExecute(s);
        }
    }

}
