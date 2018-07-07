package com.qtctek.realstate.model.user_action;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.realstate.presenter.user_action.login.PresenterImpHandleLogin;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelLogin {

    private String mUrl = MainActivity.WEB_SERVER + "user_login.php";
    private PresenterImpHandleLogin mPresenterImpHandleUserManager;

    public ModelLogin(PresenterImpHandleLogin mPresenterImpHandleUserManager){
        this.mPresenterImpHandleUserManager = mPresenterImpHandleUserManager;
    }

    public void requireCheckUserLogin(String user, String password){
        new UserLogin(user, password).execute(this.mUrl);
    }

    class UserLogin extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String user, password;

        public UserLogin(String user, String password){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.user = user;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("user", user)
                    .addFormDataPart("password", password)
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
                mPresenterImpHandleUserManager.onCheckUserLoginSuccessful(s);
            }
            else{
                mPresenterImpHandleUserManager.onCheckUserLoginError(s);
            }

            super.onPostExecute(s);
        }
    }

}
