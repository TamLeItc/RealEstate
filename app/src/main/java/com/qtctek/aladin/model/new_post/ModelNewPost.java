package com.qtctek.aladin.model.new_post;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.presenter.new_post.PresenterImpHandleModelNewPost;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModelNewPost {

    private PresenterImpHandleModelNewPost mPresenterImpHandleModelNewPost;

    private String mUrlInsertBlankPost = MainActivity.WEB_SERVER + "new_post/insert_blank_product.php";
    private String mUrlPostFile = MainActivity.WEB_SERVER + "new_post/post_file.php";
    private String mUrlUpdateProductInformation = MainActivity.WEB_SERVER + "new_post/update_product_information.php";
    private String mUrlUpdateDescriptionInformation = MainActivity.WEB_SERVER + "new_post/update_description_information.php";
    private String mUrlUpdateLocationProduct = MainActivity.WEB_SERVER + "new_post/update_location_product.php";
    private String mDeleteImage = MainActivity.WEB_SERVER + "new_post/delete_file.php";
    private String mHandlePost = MainActivity.WEB_SERVER + "new_post/update_handle_post.php";

    public ModelNewPost(PresenterImpHandleModelNewPost presenterImpHandleModelNewPost){
        this.mPresenterImpHandleModelNewPost = presenterImpHandleModelNewPost;
    }

    public void requireInsertBlankPost(int idUser){
        new InsertBlankPost(idUser).execute(mUrlInsertBlankPost);
    }

    public void requireUploadFile(int postId, String filesName, String filePath, String option){
        new PostFile(postId, filesName, filePath, option).execute(mUrlPostFile);
    }

    public void requireUpdateProductInformation(Product product){
        new UpdateProductInformation(product).execute(this.mUrlUpdateProductInformation);
    }

    public void requireUpdateDescriptionInformation(int productId, String description){
        new UpdateDescriptionInformation(productId, description).execute(this.mUrlUpdateDescriptionInformation);
    }

    public void requireUpdateLocationProduct(int productId, String mapLat, String mapLng, String option){
        new UpdateLocationProduct(productId, mapLat, mapLng, option).execute(this.mUrlUpdateLocationProduct);
    }

    public void requireDeleteFile(String linkImage, int productId){
        new DeleteFile(linkImage, productId).execute(mDeleteImage);
    }

    public void requireExecutePost(int id){
        new UpdateHandlePost(id).execute(mHandlePost);
    }

    class InsertBlankPost extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int idUser;

        public InsertBlankPost(int idUser){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.idUser = idUser;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id_user", idUser + "")
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
                mPresenterImpHandleModelNewPost.onInsertBlankPost(false, -1);
            }
            else {
                try{
                    int productId = Integer.parseInt(s.trim());
                    mPresenterImpHandleModelNewPost.onInsertBlankPost(true, productId);
                }
                catch (java.lang.NumberFormatException e){
                    mPresenterImpHandleModelNewPost.onInsertBlankPost(false, -1);
                }
            }

            super.onPostExecute(s);
        }
    }

    class UpdateProductInformation extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;
        Product product;

        public UpdateProductInformation(Product product){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.product = product;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id", product.getId() + "")
                    .addFormDataPart("formality", product.getFormality())
                    .addFormDataPart("title", product.getTitle())
                    .addFormDataPart("price", product.getPrice() + "")
                    .addFormDataPart("area", product.getArea() + "")
                    .addFormDataPart("bathroom", product.getBathroom() + "")
                    .addFormDataPart("bedroom", product.getBedroom() + "")
                    .addFormDataPart("map_lat", product.getMapLat())
                    .addFormDataPart("map_lng", product.getMapLng())
                    .addFormDataPart("amenities", product.getAmenities())
                    .addFormDataPart("address", product.getAddress())
                    .addFormDataPart("city", product.getCityId() + "")
                    .addFormDataPart("district", product.getDistrictId() + "")
                    .addFormDataPart("type", product.getTypeId() + "")
                    .addFormDataPart("architecture", product.getArchitectureId() + "")
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
                mPresenterImpHandleModelNewPost.onUpdateProductInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateProductInformation(false);
            }

            super.onPostExecute(s);
        }
    }

    class PostFile extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int postId;
        String fileName, option;
        File file;

        public PostFile(int postId, String fileName, String filePath, String option){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.postId = postId;
            this.fileName = fileName;
            this.file = new File(filePath);
            this.option = option;
        }

        @Override
        protected String doInBackground(String... strings) {

            String contentType = getType(file.getPath());

            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("uploaded_image", fileName, fileBody)
                    .addFormDataPart("product_id", postId + "")
                    .addFormDataPart("option", option)
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
                mPresenterImpHandleModelNewPost.onUploadImages(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUploadImages(false);
            }

            super.onPostExecute(s);
        }

        private String getType(String path){
            String extension = MimeTypeMap.getFileExtensionFromUrl(path);
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(extension);
        }

    }

    class UpdateDescriptionInformation extends  AsyncTask<String, Void, String>{

        private String description;
        int productId;
        OkHttpClient okHttpClient;

        public UpdateDescriptionInformation(int productId, String description){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.description = description;
            this.productId = productId;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id", productId + "")
                    .addFormDataPart("description", description + "")
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
                mPresenterImpHandleModelNewPost.onUpdateDescriptionInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateDescriptionInformation(false);
            }

            super.onPostExecute(s);
        }
    }

    class UpdateLocationProduct extends  AsyncTask<String, Void, String>{

        int productId;
        String mapLat, mapLng, option;
        OkHttpClient okHttpClient;

        public UpdateLocationProduct(int productId, String mapLat, String mapLng, String option){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.productId = productId;
            this.mapLat = mapLat;
            this.mapLng = mapLng;
            this.option = option;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id", productId + "")
                    .addFormDataPart("map_lat", mapLat)
                    .addFormDataPart("map_lng",mapLng)
                    .addFormDataPart("option", option)
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
                mPresenterImpHandleModelNewPost.onUpdateMapInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateMapInformation(false);
            }

            super.onPostExecute(s);
        }
    }

    //change status post to not accept
    class UpdateHandlePost extends  AsyncTask<String, Void, String>{

        private int postId;
        OkHttpClient okHttpClient;

        public UpdateHandlePost(int postId){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.postId = postId;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id", postId + "")
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
                mPresenterImpHandleModelNewPost.onUpdateHandlePost(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateHandlePost(false);
            }

            super.onPostExecute(s);
        }
    }

    class DeleteFile extends  AsyncTask<String, Void, String>{

        String linkImage;
        int product_id;
        OkHttpClient okHttpClient;

        public DeleteFile(String linkImage, int product_id){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.linkImage = linkImage;
            this.product_id = product_id;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("link_photo", linkImage)
                    .addFormDataPart("id_product", product_id + "")
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
            Log.d("ttt", s);
            if(s.equals("successful")){
                mPresenterImpHandleModelNewPost.onDeleteFile(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onDeleteFile(false);
            }

            super.onPostExecute(s);
        }
    }

}
