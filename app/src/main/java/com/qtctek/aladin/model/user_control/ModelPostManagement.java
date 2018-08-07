package com.qtctek.aladin.model.user_control;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.presenter.user_control.post_management.PresenterImpHandlePostManagement;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelPostManagement {

    String mUrlGetListProduct = MainActivity.WEB_SERVER + "?detect=17&";
    String mUrlUpdateAcceptPost = MainActivity.WEB_SERVER + "?detect=11&";
    String mUrlDeletePost = MainActivity.WEB_SERVER + "?detect=18&";

    private PresenterImpHandlePostManagement mPresenterImpHandlePostManagement;

    public ModelPostManagement(PresenterImpHandlePostManagement presenterImpHandlePostManagement){
        this.mPresenterImpHandlePostManagement = presenterImpHandlePostManagement;
    }

    public void requirePostListForAdmin(int start, int limit, String formality, String status){
        new GetPostList(start, limit, formality, status).execute(mUrlGetListProduct);
    }

    public void requireAcceptPost(int productId){
        new UpdateAcceptPost(productId).execute(mUrlUpdateAcceptPost);
    }

    public void requireDeletePost(int productId){
        new DeletePost(productId).execute(mUrlDeletePost);
    }

    class GetPostList extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String formality, status;
        int start, limit;

        public GetPostList(int start, int limit, String formality, String status){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.start = start;
            this.limit = limit;
            this.formality = formality;
            this.status = status;
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0] + "email=" + "&formality=" + formality + "&status=" + status
                    + "&start=" + start + "&limit=" + limit + "&option=management";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(AppUtils.USERNAME, AppUtils.USERNAME_HEADER)
                    .addHeader(AppUtils.PASSWORD, AppUtils.PASSWORD_HEADER)
                    .addHeader(AppUtils.AUTHORIZATION, AppUtils.AUTHORIZATION_HEADER)
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
            if(s.equals("error")){
                mPresenterImpHandlePostManagement.onGetPostListError(s);
            }
            else{
                mPresenterImpHandlePostManagement.onGetPostListSuccessful(s);
            }

            super.onPostExecute(s);
        }
    }

    class UpdateAcceptPost extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;
        int productId;

        public UpdateAcceptPost(int productId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.productId = productId;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("product_id", productId + "")
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .addHeader(AppUtils.USERNAME, AppUtils.USERNAME_HEADER)
                    .addHeader(AppUtils.PASSWORD, AppUtils.PASSWORD_HEADER)
                    .addHeader(AppUtils.AUTHORIZATION, AppUtils.AUTHORIZATION_HEADER)
                    .get()
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
            if(s.equals("successful")){
                mPresenterImpHandlePostManagement.onAcceptPostSuccessful();
            }
            else{
                mPresenterImpHandlePostManagement.onAcceptPostError(s);
            }

            super.onPostExecute(s);
        }
    }

    class DeletePost extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;
        int productId;

        public DeletePost(int productId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.productId = productId;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("product_id", productId + "")
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .addHeader(AppUtils.USERNAME, AppUtils.USERNAME_HEADER)
                    .addHeader(AppUtils.PASSWORD, AppUtils.PASSWORD_HEADER)
                    .addHeader(AppUtils.AUTHORIZATION, AppUtils.AUTHORIZATION_HEADER)
                    .get()
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
            if(s.equals("successful")){
                mPresenterImpHandlePostManagement.onDeletePostSuccessful();
            }
            else{
                mPresenterImpHandlePostManagement.onDeletePostError(s);
            }

            super.onPostExecute(s);
        }
    }

}
