package com.qtctek.realstate.model.user_action;

import android.os.AsyncTask;

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

    private String mUrl = MainActivity.HOST + "/real_estate/check_user_login.php";
    private PresenterImpHandleLogin mPresenterImpHandleUserManager;

    public ModelLogin(PresenterImpHandleLogin mPresenterImpHandleUserManager){
        this.mPresenterImpHandleUserManager = mPresenterImpHandleUserManager;
    }

    public void requireCheckUserLogin(String email, String password){
        new CheckUserData(email, password).execute(this.mUrl);
    }

    class CheckUserData extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String email, password;

        public CheckUserData(String userName, String password){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.email = userName;
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
            if(s.equals("-1") || s.equals("0") ||s.equals("1") || s.equals("2") || s.equals("3")){
                mPresenterImpHandleUserManager.onCheckUserLoginSuccessful(s);
            }
            else{
                mPresenterImpHandleUserManager.onCheckUserLoginError(s);
            }

            super.onPostExecute(s);
        }
    }

}
