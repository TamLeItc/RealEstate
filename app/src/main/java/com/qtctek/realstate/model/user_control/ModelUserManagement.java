package com.qtctek.realstate.model.user_control;

import android.os.AsyncTask;

import com.qtctek.realstate.presenter.user_control.user_management.PresenterImpHandleUserManagement;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelUserManagement {

    private String mUrlGetUserList = MainActivity.HOST + "/real_estate/get_user_list_for_admin.php";
    private String mUrlUpdateStatusUser = MainActivity.HOST + "/real_estate/update_status_user.php";

    private PresenterImpHandleUserManagement mPresenterImpHandleUserManagement;

    public ModelUserManagement(PresenterImpHandleUserManagement presenterImpHandleUserManagement){
        this.mPresenterImpHandleUserManagement = presenterImpHandleUserManagement;
    }

    public void requireUserList(int start, int quality){
        new GetListUser(start, quality).execute(mUrlGetUserList);
    }

    public void requireUpdateSatusUser(int userId){
        new UpdateStatusUser(userId).execute(this.mUrlUpdateStatusUser);
    }

    class GetListUser extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int start = 0, quality = 0;

        public GetListUser(int start, int quality){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.start = start;
            this.quality = quality;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("start", start + "")
                    .addFormDataPart("quality", quality + "")
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
