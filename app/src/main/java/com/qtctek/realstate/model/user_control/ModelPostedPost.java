package com.qtctek.realstate.model.user_control;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.realstate.presenter.user_control.posted_post.PresenterImpHandlePostedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelPostedPost {

    private String mUrl = MainActivity.WEB_SERVER + "get_list_product.php";

    String mUrlDeletePost = MainActivity.HOST + "/real_estate/delete_product.php";

    private PresenterImpHandlePostedPost mPresenterImpHandleUserControl;

    public ModelPostedPost(PresenterImpHandlePostedPost presenterImpHandleUserControl){
        this.mPresenterImpHandleUserControl = presenterImpHandleUserControl;
    }

    public void requirePostedPostList(int start, int limit, String email){
        new GetPostedPostList(email, start, limit).execute(mUrl);
    }

    public void requireDeletePost(int productId){
        new DeletePost(productId).execute(mUrlDeletePost);
    }

    class GetPostedPostList extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String email = "";
        int start = 0, limit = 0;

        public GetPostedPostList(String email, int start, int limit){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.email = email;
            this.start = start;
            this.limit = limit;
        }

        @Override
        protected String doInBackground(String... strings) {
            String mUrl = strings[0] + "?email=" + email + "&start=" + start + "&limit=" + limit + "&option=";

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
                mPresenterImpHandleUserControl.onGetPostListError(s);

            }
            else{
                mPresenterImpHandleUserControl.onGetPostListSuccessful(s);
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
            if(s.equals("successful")){
                mPresenterImpHandleUserControl.onDeletePostSuccessful();
            }
            else{
                mPresenterImpHandleUserControl.onDeletePostError(s);
            }

            super.onPostExecute(s);
        }
    }

}
