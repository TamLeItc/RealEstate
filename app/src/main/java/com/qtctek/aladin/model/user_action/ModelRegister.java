package com.qtctek.aladin.model.user_action;

import android.os.AsyncTask;

import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.presenter.user_action.register.PresenterImpHandleRegister;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelRegister {

    private String mUrl = MainActivity.WEB_SERVER + "?detect=14&";

    private PresenterImpHandleRegister mPresenterImpHandleRegister;

    public ModelRegister(PresenterImpHandleRegister presenterImpHandleRegister){
        this.mPresenterImpHandleRegister = presenterImpHandleRegister;
    }

    public void requireCheckEmail(String email, String username){
        new CheckEmail(email, username, "check_user").execute(mUrl);
    }

    public void requireInsertUser(User user){
        new UserRegister(user, "insert").execute(mUrl);
    }

    class CheckEmail extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String email, username, option;

        public CheckEmail(String email, String username, String option){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.email = email;
            this.option = option;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email", email)
                    .addFormDataPart("username", username)
                    .addFormDataPart("option", option)
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
                return  e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("not_exist") || s.equals("email_existed") || s.equals("username_existed")){
                mPresenterImpHandleRegister.onCheckExistEmail(s);
            }
            else{
                mPresenterImpHandleRegister.onConnectServerError(s);
            }

            super.onPostExecute(s);
        }
    }

    class UserRegister extends AsyncTask<String, Void, String>{


        OkHttpClient okHttpClient;

        User user = null;
        String option;

        public UserRegister(User user, String option){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.user = user;
            this.option = option;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("name", user.getFullName())
                    .addFormDataPart("sex", user.getSex())
                    .addFormDataPart("birthday", user.getBirthDay())
                    .addFormDataPart("phone", user.getPhone())
                    .addFormDataPart("email", user.getEmail())
                    .addFormDataPart("address", user.getAddress())
                    .addFormDataPart("username", user.getUsername())
                    .addFormDataPart("password", user.getPassword())
                    .addFormDataPart("option", option)
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
                return  response.body().string();
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("successful")){
                mPresenterImpHandleRegister.onInsertValueToServerSuccessful();
            }
            else{
                mPresenterImpHandleRegister.onInsertValueToServerError(s);
            }
            super.onPostExecute(s);
        }
    }

}
