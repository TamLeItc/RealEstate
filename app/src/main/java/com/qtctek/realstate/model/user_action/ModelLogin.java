package com.qtctek.realstate.model.user_action;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
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

    public void requireGetDataSaveLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(AppUtils.USERNAME, "");
        String password = sharedPreferences.getString(AppUtils.PASSWORD, "");

        this.mPresenterImpHandleUserManager.onGetDataSaveLoginSuccessful(userName, password);
    }

    public void requireUpdateDataSaveLogin(String userName, String password, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppUtils.USERNAME, userName);
        editor.putString(AppUtils.PASSWORD, password);
        editor.commit();

        this.mPresenterImpHandleUserManager.onUpdateDataSaveLoginSuccessful();
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
