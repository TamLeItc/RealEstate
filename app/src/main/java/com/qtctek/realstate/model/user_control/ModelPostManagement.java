package com.qtctek.realstate.model.user_control;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.qtctek.realstate.presenter.user_control.post_management.PresenterImpHandlePostManagement;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelPostManagement {

    String mUrlGetListProduct = MainActivity.WEB_SERVER + "get_list_product.php";

    String mUrlUpdateAcceptPost = MainActivity.HOST + "/real_estate/update_accept_post.php";

    String mUrlDeletePost = MainActivity.HOST + "/real_estate/delete_product.php";

    private PresenterImpHandlePostManagement mPresenterImpHandlePostManagement;

    public ModelPostManagement(PresenterImpHandlePostManagement presenterImpHandlePostManagement){
        this.mPresenterImpHandlePostManagement = presenterImpHandlePostManagement;
    }

    public void requirePostListForAdmin(int start, int limit){
        new GetPostList(start, limit).execute(mUrlGetListProduct);
    }

    public void requireAcceptPost(int productId){
        new UpdateAcceptPost(productId).execute(mUrlUpdateAcceptPost);
    }

    public void requireDeletePost(int productId){
        new DeletePost(productId).execute(mUrlDeletePost);
    }

    class GetPostList extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int start = 0, limit = 0;

        public GetPostList(int start, int limit){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.start = start;
            this.limit = limit;
        }

        @Override
        protected String doInBackground(String... strings) {

            String mUrl = strings[0] + "?email=%" + "&start=" + start + "&limit=" + limit + "&option=";
            Request request = new Request.Builder()
                    .url(mUrl)
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
            Log.d("ttt", s);
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
