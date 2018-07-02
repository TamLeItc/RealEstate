package com.qtctek.realstate.model.new_post;

import android.os.AsyncTask;

import com.qtctek.realstate.presenter.new_post.GetData.PresenterImpHandleModelGetData;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelGetData {

    private PresenterImpHandleModelGetData mPresenterImpHandleModelGetData;

    String mUrlProvinceCity = MainActivity.HOST + "/real_estate/get_province_city.php";
    String mUrlDistrict = MainActivity.HOST + "/real_estate/get_district.php";
    String mUrlCategoriesProduct = MainActivity.HOST + "/real_estate/get_categories_product.php";

    public ModelGetData(PresenterImpHandleModelGetData presenterImpHandleModelGetData){
        this.mPresenterImpHandleModelGetData = presenterImpHandleModelGetData;
    }

    public void requireGetProvinceCity(){
        new GetProvinceCity().execute(mUrlProvinceCity);
    }

    public void requireGetDistrict(int provinceCityId){
        new GetDistrict(provinceCityId).execute(mUrlDistrict);
    }

    public void requireGetCategoriesProduct(){
        new GetCategoriesProduct().execute(mUrlCategoriesProduct);
    }

    class GetProvinceCity extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        public GetProvinceCity(){
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
                mPresenterImpHandleModelGetData.onGetProvinceCity(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetProvinceCity(false, s);
            }

            super.onPostExecute(s);
        }
    }

    class GetDistrict extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int provinceCityId;

        public GetDistrict(int provinceCityId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.provinceCityId = provinceCityId;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id_province_city", provinceCityId + "")
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
                mPresenterImpHandleModelGetData.onGetDistrict(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetDistrict(false, s);
            }

            super.onPostExecute(s);
        }
    }

    class GetCategoriesProduct extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        public GetCategoriesProduct(){
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
                mPresenterImpHandleModelGetData.onGetCategoriesProduct(true, s);
            }
            else{
                mPresenterImpHandleModelGetData.onGetCategoriesProduct(false, s);
            }

            super.onPostExecute(s);
        }
    }

}
