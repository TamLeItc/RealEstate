package com.qtctek.realstate.model.user_action;

import android.os.AsyncTask;
import android.util.Log;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.presenter.user_action.update_user.PresenterImpHandleUpdateUser;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelUpdateUser {

    private PresenterImpHandleUpdateUser mPresenterImpHandleUpdateUser;

    private String mUrlUpdateUser = MainActivity.WEB_SERVER + "udpate_user.php";

    public ModelUpdateUser(PresenterImpHandleUpdateUser presenterImpHandleUpdateUser){
        this.mPresenterImpHandleUpdateUser = presenterImpHandleUpdateUser;
    }

    public void requireUpdateUser(User user, String oldPassword){
        new UpdateUser(user, oldPassword).execute(mUrlUpdateUser);
    }

    class UpdateUser extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        User user;
        String oldPassword;

        public UpdateUser(User user, String oldPassword){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.user = user;
            this.oldPassword = oldPassword;
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
                    .addFormDataPart("old_password", oldPassword)
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
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("successful")){
                mPresenterImpHandleUpdateUser.onUpdateUserSuccessful();
            }
            else{
                mPresenterImpHandleUpdateUser.onUpdateUserError(s);
            }
            super.onPostExecute(s);
        }
    }



}
