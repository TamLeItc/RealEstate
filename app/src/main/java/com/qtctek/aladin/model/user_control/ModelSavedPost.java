package com.qtctek.aladin.model.user_control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.presenter.user_control.saved_post.PresenterImpIHandleSavedPost;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModelSavedPost {

    private PresenterImpIHandleSavedPost mPresenterImPHandleSavedPost;

    String mUrlGetListProduct = MainActivity.WEB_SERVER + "?detect=17&";

    public ModelSavedPost(PresenterImpIHandleSavedPost mPresenterImPHandleSavedPost){
        this.mPresenterImPHandleSavedPost = mPresenterImPHandleSavedPost;
    }

    public void requireDataProductIds( Context context){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
            String data = sharedPreferences.getString(Constant.SAVED_PRODUCT_LIST, "");
            mPresenterImPHandleSavedPost.onGetDataProductIdsSuccessful(data);
        }
        catch (Exception e) {
            mPresenterImPHandleSavedPost.onGetDataProductIdsError(e.toString());
        }
    }

    public void requireUpdateDataProductIds(String data, Context context){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(Constant.SAVED_PRODUCT_LIST, data);
            editor.commit();
            mPresenterImPHandleSavedPost.onUpdateProductIdListSuccessful();
        } catch (Exception e) {
            mPresenterImPHandleSavedPost.onUpdateProductIdListError(e.toString());
            e.printStackTrace();
        }
    }

    public void requireSavedProductList(int start, int limit, String listId){
        new SavedPost(start, limit, listId).execute(mUrlGetListProduct);
    }

    class SavedPost extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient = new OkHttpClient();

        int start, limit;
        String listId;
        public SavedPost (int start, int limit, String listId){
            this.start = start;
            this.limit = limit;
            this.listId = listId;
        }

        @Override
        protected String doInBackground(String... strings) {
            String mUrl = strings[0] + "email=%" + "&start=" + start + "&limit=" + limit + "&option=saved_product"
                    + "&list_id=" + listId;
            Request request = new Request.Builder()
                    .url(mUrl)
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
                mPresenterImPHandleSavedPost.onGetSavedProductListError(s);
            }
            else{
                mPresenterImPHandleSavedPost.onGetSavedProductListSuccessful(s);
            }
        }
    }
}
