package com.qtctek.realstate.presenter.new_post;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
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

    public void handlePostImage(int postId, String fileName, String filePath){
        this.mModelNewPost.requireUploadFile(postId, fileName, filePath);
    }

    public void handleUpdateNormalInformation(Product product){
        this.mModelNewPost.requireUpdateNormalInformation(product);
    }

    public void handleUpdateMoreInformation(Product product){
        this.mModelNewPost.requireUpdateMoreInformation(product);
    }

    public void handleUpdateDescriptionInformation(Product product){
        this.mModelNewPost.requireUpdateDescriptionInformation(product);
    }

    public void handleUpdateLocationProduct(Product product){
        this.mModelNewPost.requireUpdateLocationProduct(product);
    }

    public void handleUpdateContactInformation(PostSale postSale){
        this.mModelNewPost.requireUpdateContactInformation(postSale);
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
    public void onInsertBlankPost(boolean status, int postId) {
        this.mViewHandleModelNewPost.onInsertBlankPost(status, postId);
    }

    @Override
    public void onUploadImages(boolean status) {
        this.mViewHandleModelNewPost.onUploadImages(status);
    }

    @Override
    public void onUpdateNormalInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateNormalInformation(status);
    }

    @Override
    public void onUpdateMoreInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateMoreInformation(status);
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
    public void onUpdateContactInformation(boolean status) {
        this.mViewHandleModelNewPost.onUpdateContactInformation(status);
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
