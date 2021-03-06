package com.qtctek.aladin.model.user_control;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.presenter.user_control.user_management.PresenterImpHandleUserManagement;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelUserManagement {

    private String mUrlGetUserList = MainActivity.WEB_SERVER + "?detect=19&";
    private String mUrlUpdateStatusUser = MainActivity.WEB_SERVER + "?detect=20&";

    private PresenterImpHandleUserManagement mPresenterImpHandleUserManagement;

    public ModelUserManagement(PresenterImpHandleUserManagement presenterImpHandleUserManagement){
        this.mPresenterImpHandleUserManagement = presenterImpHandleUserManagement;
    }

    public void requireUserList(int start, int limit, String status){
        new GetListUser(start, limit, status).execute(mUrlGetUserList);
    }

    public void requireUpdateSatusUser(int userId){
        new UpdateStatusUser(userId).execute(this.mUrlUpdateStatusUser);
    }

    class GetListUser extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int start = 0, limit = 0;
        String status;

        public GetListUser(int start, int limit, String status){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.start = start;
            this.limit = limit;
            this.status = status;
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0] + "limit=" + limit + "&start=" + start + "&status=" + status;
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
                mPresenterImpHandleUserManagement.onGetUserListError(s);
            }
            else{
                mPresenterImpHandleUserManagement.onGetUserListSuccessful(s);
            }
            super.onPostExecute(s);
        }
    }

    class UpdateStatusUser extends AsyncTask<String, Void, String>
    {
        OkHttpClient okHttpClient;
        int user_id;

        public UpdateStatusUser(int user_id){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.user_id = user_id;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("user_id", user_id + "")
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
                mPresenterImpHandleUserManagement.onUpdateStatusUserSuccessful();
            }
            else{
                mPresenterImpHandleUserManagement.onUpdateStatusUserError(s);
            }

            super.onPostExecute(s);
        }
    }

}
