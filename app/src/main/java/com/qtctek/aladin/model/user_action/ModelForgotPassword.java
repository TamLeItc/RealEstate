package com.qtctek.aladin.model.user_action;

import android.os.AsyncTask;

import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.presenter.user_action.forgot_password.PresenterForgotPassword;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelForgotPassword {

    String mUrl = MainActivity.WEB_SERVER + "?detect=12&";

    private PresenterForgotPassword mPresenterForgotPassword;

    public ModelForgotPassword(PresenterForgotPassword mPresenterForgotPassword){
        this.mPresenterForgotPassword = mPresenterForgotPassword;
    }

    public void requireUpdatePassword(String email, String password){
        new UpdatePassword(email, password).execute(mUrl);
    }


    class UpdatePassword extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient;

        String email, password;

        public UpdatePassword(String email, String password){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
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
                e.printStackTrace();
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("successful")){
                mPresenterForgotPassword.onUpdatePasswordSuccessful();
            }
            else{
                mPresenterForgotPassword.onUpdatePasswordError();
            }

            super.onPostExecute(s);
        }
    }

}
