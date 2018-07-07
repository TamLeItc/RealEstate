package com.qtctek.realstate.presenter.new_post;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.Product1;
import com.qtctek.realstate.model.new_post.ModelNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PresenterNewPost  implements PresenterImpHandleModelNewPost{

    private ViewHandleModelNewPost mViewHandleModelNewPost;
    private ModelNewPost mModelNewPost;

    public PresenterNewPost(ViewHandleModelNewPost viewHandleModelNewPost){
        this.mViewHandleModelNewPost = viewHandleModelNewPost;
        this.mModelNewPost = new ModelNewPost(this);
    }

    public void handleInsertBlankPost(String email){
        this.mModelNewPost.requireInsertBlankPost(email, getCurrentDate());

    }

    public void handlePostImage(int postId, String fileName, String filePath, String option){
        this.mModelNewPost.requireUploadFile(postId, fileName, filePath, option);
    }

    public void handleUpdateProductInformation(Product product){
        this.mModelNewPost.requireUpdateProductInformation(product);
    }

    public void handleUpdateDescriptionInformation(int productId, String description){
        this.mModelNewPost.requireUpdateDescriptionInformation(productId, description);
    }

    public void handleUpdateLocationProduct(int productId, String mapLat, String mapLng){
        this.mModelNewPost.requireUpdateLocationProduct(productId, mapLat, mapLng);
    }

    public void handleDeleteFile(String linkImage){
        this.mModelNewPost.requireDeleteFile(linkImage);
    }

    public void handleExcutePost(int id){
        this.mModelNewPost.requireExcutePost(id);
    }

    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    @Override
    public void onInsertBlankPost(boolean status, int productId) {
        this.mViewHandleModelNewPost.onInsertBlankPost(status, productId);
    }

    @Override
    public void onUploadImages(boolean status) {
        this.mViewHandleModelNewPost.onUploadImages(status);
    }

    @Override
    public void onUpdateProductInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateProductInformation(status);
    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateDescriptionInformation(status);
    }

    @Override
    public void onUpdateMapInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateMapInformation(status);
    }

    @Override
    public void onDeleteFile(boolean status) {
        this.mViewHandleModelNewPost.onDeleteFile(status);
    }

    @Override
    public void onUpdateHandlePost(boolean status) {
        mViewHandleModelNewPost.onUpdateHandlePost(status);
    }


}
