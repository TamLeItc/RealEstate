package com.qtctek.realstate.presenter.new_post;

import android.util.Log;

import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.new_post.ModelNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class PresenterNewPost  implements PresenterImpHandleModelNewPost{

    private ViewHandleModelNewPost mViewHandleModelNewPost;
    private ModelNewPost mModelNewPost;

    public PresenterNewPost(ViewHandleModelNewPost viewHandleModelNewPost){
        this.mViewHandleModelNewPost = viewHandleModelNewPost;
        this.mModelNewPost = new ModelNewPost(this);
    }

    public void handleInsertBlankPost(int idUser){
        this.mModelNewPost.requireInsertBlankPost(idUser);

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

    public void handleUpdateLocationProduct(int productId, String mapLat, String mapLng, String option){
        this.mModelNewPost.requireUpdateLocationProduct(productId, mapLat, mapLng, option);
    }

    public void handleDeleteFile(String linkImage){
        this.mModelNewPost.requireDeleteFile(linkImage);
    }

    public void handleExecutePost(int id){
        this.mModelNewPost.requireExecutePost(id);
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
