package com.qtctek.realstate.model.new_post;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.new_post.PresenterImpHandleModelNewPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

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

    private String mUrlInsertBlankPost = MainActivity.HOST + "/real_estate/new_post/insert_blank_post.php";
    private String mUrlPostFile = MainActivity.HOST + "/real_estate/new_post/post_file.php";
    private String mUrlUPdateNormalInformation = MainActivity.HOST + "/real_estate/new_post/update_normal_information.php";
    private String mUrlUpdateMoreInformation = MainActivity.HOST + "/real_estate/new_post/update_product_detail.php";
    private String mUrlUpdateDescriptionInformation = MainActivity.HOST + "/real_estate/new_post/update_description_information.php";
    private String mUrlUpdateLocationProduct = MainActivity.HOST + "/real_estate/new_post/update_location_product.php";
    private String mUrlUpdateContactInformation = MainActivity.HOST + "/real_estate/new_post/update_contact_information.php";
    private String mDeleteImage = MainActivity.HOST + "/real_estate/new_post/delete_file.php";
    private String mHandlePost = MainActivity.HOST + "/real_estate/new_post/update_handle_post.php";

    public ModelNewPost(PresenterImpHandleModelNewPost presenterImpHandleModelNewPost){
        this.mPresenterImpHandleModelNewPost = presenterImpHandleModelNewPost;
    }

    public void requireInsertBlankPost(String email, String postDate){
        new InsertBlankPost(email, postDate).execute(mUrlInsertBlankPost);
    }

    public void requireUploadFile(int postId, String filesName, String filePath){
        new PostFile(postId, filesName, filePath).execute(mUrlPostFile);
    }

    public void requireUpdateNormalInformation(Product product){
        new UpdateNormalInformation(product).execute(this.mUrlUPdateNormalInformation);
    }

    public void requireUpdateMoreInformation(Product product){
        new UpdateMoreInformation(product).execute(this.mUrlUpdateMoreInformation);
    }

    public void requireUpdateDescriptionInformation(Product product){
        new UpdateDescriptionInformation(product).execute(this.mUrlUpdateDescriptionInformation);
    }

    public void requireUpdateLocationProduct(Product product){
        new UpdateLocationProduct(product).execute(this.mUrlUpdateLocationProduct);
    }

    public void requireUpdateContactInformation(PostSale postSale){
        new UpdateContactInformation(postSale).execute(this.mUrlUpdateContactInformation);
    }

    public void requireDeleteFile(String linkImage){
        new DeleteFile(linkImage).execute(mDeleteImage);
    }

    public void requireExcutePost(int id){
        new UpdateHandlePost(id).execute(mHandlePost);
    }

    class InsertBlankPost extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        String email, postDate;

        public InsertBlankPost(String email, String postDate){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.email = email;
            this.postDate = postDate;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email", email)
                    .addFormDataPart("post_date", postDate)
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
                mPresenterImpHandleModelNewPost.onInsertBlankPost(false, 0);
            }
            else {
                try{
                    int postId = Integer.parseInt(s.trim());
                    mPresenterImpHandleModelNewPost.onInsertBlankPost(true, postId);
                }
                catch (java.lang.NumberFormatException e){
                    mPresenterImpHandleModelNewPost.onInsertBlankPost(false, 0);
                }
            }

            super.onPostExecute(s);
        }
    }

    class UpdateNormalInformation extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;
        Product product;

        public UpdateNormalInformation(Product product){

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
                    .addFormDataPart("category_product_id", product.getCategoryProductId() + "")
                    .addFormDataPart("area", product.getArea() + "")
                    .addFormDataPart("bathrooms", product.getBathrooms() + "")
                    .addFormDataPart("bedrooms", product.getBedrooms() + "")
                    .addFormDataPart("district_id", product.getDistrictId() + "")
                    .addFormDataPart("province_city_id", product.getProvinceCityId() + "")
                    .addFormDataPart("address", product.getAddress())
                    .addFormDataPart("price", product.getPrice() + "")
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
                mPresenterImpHandleModelNewPost.onUpdateNormalInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateNormalInformation(false);
            }

            super.onPostExecute(s);
        }
    }

    class PostFile extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient;

        int postId;
        String fileName;
        File file;

        public PostFile(int postId, String fileName, String filePath){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            this.postId = postId;
            this.fileName = fileName;
            this.file = new File(filePath);
        }

        @Override
        protected String doInBackground(String... strings) {

            String contentType = getType(file.getPath());

            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("uploaded_image", fileName, fileBody)
                    .addFormDataPart("post_id", postId + "")
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

    class UpdateMoreInformation extends  AsyncTask<String, Void, String>{

        private Product product;
        OkHttpClient okHttpClient;

        public UpdateMoreInformation(Product product){
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
                    .addFormDataPart("floors", product.getProductDetail().getFloors() + "")
                    .addFormDataPart("legal", product.getProductDetail().getLegal())
                    .addFormDataPart("utility", product.getProductDetail().getUtility())
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
                mPresenterImpHandleModelNewPost.onUpdateMoreInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateMoreInformation(false);
            }

            super.onPostExecute(s);
        }
    }

    class UpdateDescriptionInformation extends  AsyncTask<String, Void, String>{

        private Product product;
        OkHttpClient okHttpClient;

        public UpdateDescriptionInformation(Product product){
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
                    .addFormDataPart("description", product.getDescription() + "")
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

        private Product product;
        OkHttpClient okHttpClient;

        public UpdateLocationProduct(Product product){
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
                    .addFormDataPart("latitude", product.getLatitude() + "")
                    .addFormDataPart("longitude", product.getLongitude())
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

    class UpdateContactInformation extends  AsyncTask<String, Void, String>{

        private PostSale postSale;
        OkHttpClient okHttpClient;

        public UpdateContactInformation(PostSale postSale){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.postSale = postSale;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("id", postSale.getId() + "")
                    .addFormDataPart("contact_name", postSale.getContactName() + "")
                    .addFormDataPart("contact_number_phone", postSale.getContactNumberPhone() + "")
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
                mPresenterImpHandleModelNewPost.onUpdateContactInformation(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onUpdateContactInformation(false);
            }

            super.onPostExecute(s);
        }
    }

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
        OkHttpClient okHttpClient;

        public DeleteFile(String linkImage){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            this.linkImage = linkImage;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("link_image", linkImage)
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
                mPresenterImpHandleModelNewPost.onDeleteFile(true);
            }
            else{
                mPresenterImpHandleModelNewPost.onDeleteFile(false);
            }

            super.onPostExecute(s);
        }
    }

}
