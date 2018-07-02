package com.qtctek.realstate.model.user_control;

import android.os.AsyncTask;

import com.qtctek.realstate.presenter.user_control.saved_post.PresenterImpIHandleSavedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelSavedPost {

    private String mUrlGetSavedPostList = MainActivity.HOST + "/real_estate/get_saved_post_list.php";
    private String mUrlUnSavePost = MainActivity.HOST + "/real_estate/unsave_post.php";

    private PresenterImpIHandleSavedPost mPresenterImPHandleSavedPost;

    public ModelSavedPost(PresenterImpIHandleSavedPost mPresenterImPHandleSavedPost){
        this.mPresenterImPHandleSavedPost = mPresenterImPHandleSavedPost;
    }

    public void requireSavedPostList(String email, int start, int quality){
        new GetSavedPostList(email, start, quality).execute(mUrlGetSavedPostList);
    }

    public void requireUnSavePost(int id){
        new ExecuteUnSavePost(id).execute(mUrlUnSavePost);
    }

    class GetSavedPostList extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient;

        String email = "";
        int start = 0, quality = 0;

        public GetSavedPostList(String email, int start, int quality){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.email = email;
            this.start = start;
            this.quality = quality;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email", email)
                    .addFormDataPart("start", start + "")
                    .addFormDataPart("quality", quality + "")
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
            if(s.equals("error")){
                mPresenterImPHandleSavedPost.onHandleSavedPostListError(s);

            }
            else{
                mPresenterImPHandleSavedPost.onGetSavedPostListSuccessful(s);
            }

            super.onPostExecute(s);
        }
    }

    class ExecuteUnSavePost extends AsyncTask<String, Void, String>{
        OkHttpClient okHttpClient;

        int postSaleId;

        public ExecuteUnSavePost(int postSaleId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.postSaleId = postSaleId;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("post_sale_id", postSaleId + "")
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
                mPresenterImPHandleSavedPost.onExecuteUnSavePostSuccessful();

            }
            else{
                mPresenterImPHandleSavedPost.onExecuteUnSavePostError(s);
            }

            super.onPostExecute(s);
        }
    }

}
