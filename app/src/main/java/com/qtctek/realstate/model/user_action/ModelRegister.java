package com.qtctek.realstate.model.user_action;

import android.os.AsyncTask;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.presenter.user_action.register.PresenterImpHandleRegister;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelRegister {

    String mUrl = MainActivity.HOST + "/real_estate/user_register.php";

    private PresenterImpHandleRegister mPresenterImpHandleRegister;

    public ModelRegister(PresenterImpHandleRegister presenterImpHandleRegister){
        this.mPresenterImpHandleRegister = presenterImpHandleRegister;
    }

    public void requireCheckEmail(String email){
        new CheckEmail(email).execute(mUrl);
    }

    public void requireInsertUser(User user){
        new UserRegister(user, "insert").execute(mUrl);
    }

    class CheckEmail extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String email, option;

        public CheckEmail(String email){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.email = email;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email", email)
                    .addFormDataPart("option", "check_email")
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return  response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("existed")){
                mPresenterImpHandleRegister.onCheckExistEmail(true);
            }
            else if(s.equals("not_exist")){
                mPresenterImpHandleRegister.onCheckExistEmail(false);
            }
            else{
                mPresenterImpHandleRegister.onConnectServerError();
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
                    .addFormDataPart("name", user.getName())
                    .addFormDataPart("email", user.getEmail())
                    .addFormDataPart("password", user.getPassword())
                    .addFormDataPart("phone_number", user.getPhoneNumber())
                    .addFormDataPart("option", option)
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return  response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
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
