package com.qtctek.realstate.model.user_action;

import android.os.AsyncTask;

import com.qtctek.realstate.presenter.user_action.update_user.PresenterImpHandleUpdateUser;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelInformationUser {

    private PresenterImpHandleUpdateUser mPresenterImpHandleUpdateUser;

    private String mUrl = MainActivity.HOST + "/real_estate/get_information_user.php";

    public ModelInformationUser(PresenterImpHandleUpdateUser presenterImpHandleUpdateUser){
        this.mPresenterImpHandleUpdateUser = presenterImpHandleUpdateUser;
    }

    public void requireGetInformationUser(String email){
        new UpdateUser(email).execute(mUrl);
    }

    class UpdateUser extends AsyncTask<String, Void, String> {

        OkHttpClient okHttpClient;

        String email;

        public UpdateUser(String email){
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
            if(!s.equals("error")){
                mPresenterImpHandleUpdateUser.onGetInformationUserSuccessful(s);
            }
            else{
                mPresenterImpHandleUpdateUser.onGetInformationUserError(s);
            }

            super.onPostExecute(s);
        }
    }

}
