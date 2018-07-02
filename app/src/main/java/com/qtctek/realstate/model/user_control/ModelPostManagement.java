package com.qtctek.realstate.model.user_control;

import android.os.AsyncTask;

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

    String mUrlGetListPost = MainActivity.HOST + "/real_estate/get_post_list_for_admin.php";

    String mUrlUpdateAcceptPost = MainActivity.HOST + "/real_estate/update_accept_post.php";

    String mUrlDeletePost = MainActivity.HOST + "/real_estate/delete_post.php";

    private PresenterImpHandlePostManagement mPresenterImpHandlePostManagement;

    public ModelPostManagement(PresenterImpHandlePostManagement presenterImpHandlePostManagement){
        this.mPresenterImpHandlePostManagement = presenterImpHandlePostManagement;
    }

    public void requirePostListForAdmin(int start, int quality, int isNotActive){
        new GetPostList(start, quality, isNotActive).execute(mUrlGetListPost);
    }

    public void requireAcceptPost(int postId){
        new UpdateAcceptPost(postId).execute(mUrlUpdateAcceptPost);
    }

    public void requireDeletePost(int postId){
        new DeletePost(postId).execute(mUrlDeletePost);
    }

    class GetPostList extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int start = 0, quality = 0, post_status = 0;

        public GetPostList(int start, int quality, int status){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.start = start;
            this.quality = quality;
            this.post_status = status;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("start", start + "")
                    .addFormDataPart("quality", quality + "")
                    .addFormDataPart("status", post_status + "")
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
        int postId;

        public UpdateAcceptPost(int postId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.postId = postId;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("post_id", postId + "")
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
        int postId;

        public DeletePost(int postId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.postId = postId;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id_post", postId + "")
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
                mPresenterImpHandlePostManagement.onDeletePostSuccessful();
            }
            else{
                mPresenterImpHandlePostManagement.onDeletePostError(s);
            }

            super.onPostExecute(s);
        }
    }

}
