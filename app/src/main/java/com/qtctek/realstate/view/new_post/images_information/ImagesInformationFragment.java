package com.qtctek.realstate.view.new_post.images_information;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.interfaces.OnRequireHandleFromAdapter;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ImagesInformationFragment extends Fragment implements ViewHandleModelNewPost, OnRequireHandleFromAdapter,
        View.OnClickListener {

    private View mView;

    private Button mBtnSelectImage;
    private Button mBtnSelectAvartar;
    private Button mBtnNext;
    private Button mBtnSaveTemp;
    public static ProgressBar PROGRESSBAR;
    private RecyclerView mRecyclerView;
    public static ImageView IMV_AVARTAR;

    public static ImageAdapter IMAGE_ADAPTER;
    public static ArrayList<Photo> ARR_PHOTO = new ArrayList<>();
    public static int FILE_NAME = 1;

    private boolean mIsSaveTemp;
    private int mPosition = 0;
    private int mQualityImageUploaded;
    private boolean mIsPickAvartar = false;
    private boolean mIsPickImage = false;
    private String mPath;
    private Uri mUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_images_information, container, false);

        initViews();
        handleStart();

        return mView;
    }

    private void initViews() {
        this.mBtnSelectImage = mView.findViewById(R.id.btn_select_image);
        this.mBtnSelectAvartar = mView.findViewById(R.id.btn_select_avartar);
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mBtnSaveTemp = mView.findViewById(R.id.btn_save_temp);
        this.mRecyclerView = mView.findViewById(R.id.recycler_view);
        IMV_AVARTAR = mView.findViewById(R.id.imv_avartar);
        this.PROGRESSBAR = mView.findViewById(R.id.progress_bar);

        this.mBtnSelectImage.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mBtnSelectAvartar.setOnClickListener(this);
        this.mBtnSaveTemp.setOnClickListener(this);
    }

    private void handleStart(){
        this.IMAGE_ADAPTER = new ImageAdapter(getContext(), ARR_PHOTO, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerView.setLayoutManager(layoutManager);

        this.mRecyclerView.setAdapter(IMAGE_ADAPTER);

        try{
            String url = MainActivity.WEB_SERVER + "/images/" + NewPostActivity.PRODUCT.getThumbnail();
            PROGRESSBAR.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(url).into(IMV_AVARTAR, new Callback() {
                @Override
                public void onSuccess() {
                    PROGRESSBAR.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    PROGRESSBAR.setVisibility(View.GONE);
                    IMV_AVARTAR.setImageResource(R.drawable.icon_product);
                }
            });
        }
        catch (java.lang.NullPointerException e){}
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void handleResultAvartar(){
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(mUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            IMV_AVARTAR.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void handleResultImage(Intent data) {
        ClipData mClipData = data.getClipData();
        for (int i = 0; i < mClipData.getItemCount(); i++) {

            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            ARR_PHOTO.add(new Photo(uri));
        }
        this.IMAGE_ADAPTER.notifyDataSetChanged();
    }

    private void uploadAvartar(){
        ((NewPostActivity)getActivity()).dialogHelper.show();

        mPath = getRealPathFromURI(getContext(), mUri);

        String fileName = NewPostActivity.PRODUCT.getId() + "_avartar.jpg";
        new PresenterNewPost(this).handlePostImage(NewPostActivity.PRODUCT.getId(),
                fileName, mPath, "thumbnail");
    }

    private void uploadImages() {
        while(!ARR_PHOTO.get(mQualityImageUploaded).getIsUpload()){
            mQualityImageUploaded++;
            if(mQualityImageUploaded > ARR_PHOTO.size() - 1){
                return;
            }
        }
        Uri uri = ARR_PHOTO.get(mQualityImageUploaded).getUri();
        String path = getRealPathFromURI(getContext(), uri);

        String fileName = NewPostActivity.PRODUCT.getId() + "_" + (FILE_NAME++) + ".jpg";
        new PresenterNewPost(this).handlePostImage(NewPostActivity.PRODUCT.getId(),
                fileName, path, "image_detail");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            mIsPickImage = true;
            if (data.getData() != null) {
                Uri uri = data.getData();
                ARR_PHOTO.add(new Photo(uri));
                this.IMAGE_ADAPTER.notifyDataSetChanged();
            } else if (data.getClipData() != null) {
                handleResultImage(data);
            }
        }
        else if(requestCode == 102 && resultCode == RESULT_OK && data != null){
            this.mUri = data.getData();
            this.mIsPickAvartar = true;

            handleResultAvartar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSelectImages(){
        int check = checkPermission();
        if(check == 1){
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), 101);
        }
        else if(check == 0){
            requestPermissions(101);
        }
    }

    private void handleSelectAvartar(){
        int check = checkPermission();
        if(check == 1){
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1, 102);
        }
        else if(check == 0){
            requestPermissions(102);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_image:
                handleSelectImages();
                break;
            case R.id.btn_select_avartar:
                handleSelectAvartar();
                break;
            case R.id.btn_next_to:
                this.mIsSaveTemp = false;
                mQualityImageUploaded = 0;
                handleSave();
                break;
            case R.id.btn_save_temp:
                mQualityImageUploaded = 0;
                this.mIsSaveTemp = true;
                handleSave();
                break;
        }
    }

    private void handleSave(){
        if(mIsPickImage && !mIsPickAvartar){
            if (mIsPickImage) {
                ((NewPostActivity)getActivity()).dialogHelper.show();
                uploadImages();
            }
        }
        else if(mIsPickAvartar){
            ((NewPostActivity)getActivity()).dialogHelper.show();
            uploadAvartar();
        }
        else{
            if(!mIsSaveTemp){
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
                ((NewPostActivity)getActivity()).setCurrentStateNumberProgressBar(
                        ((NewPostActivity) getActivity()).viewPaper.getCurrentItem());
            }
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

    }

    @Override
    public void onUploadImages(boolean status) {
        if(mIsPickAvartar){
            ((NewPostActivity)getActivity()).dialogHelper.dismiss();
            if (!status) {
                ((NewPostActivity)getActivity()).toastHelper.toast("Lỗi lưu dữ liệu", ToastHelper.LENGTH_SHORT);
            } else {
                mIsPickAvartar = false;

                if(mIsPickImage) {
                    uploadImages();
                }
                else{
                    ((NewPostActivity)getActivity()).toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);
                    if(!mIsSaveTemp){
                        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(1);
                        ((NewPostActivity)getActivity()).setCurrentStateNumberProgressBar(
                                ((NewPostActivity) getActivity()).viewPaper.getCurrentItem());
                    }

                }
            }
        }
        else{
            mQualityImageUploaded += 1;
            if(ARR_PHOTO.size() == mQualityImageUploaded){
                ((NewPostActivity)getActivity()).dialogHelper.dismiss();
                if (!status) {
                    ((NewPostActivity)getActivity()).toastHelper.toast("Lỗi lưu dữ liệu", ToastHelper.LENGTH_SHORT);
                } else {
                    ((NewPostActivity)getActivity()).toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);

                    if(!mIsSaveTemp){
                        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(1);
                        ((NewPostActivity)getActivity()).setCurrentStateNumberProgressBar(
                                ((NewPostActivity) getActivity()).viewPaper.getCurrentItem());
                    }
                }
                mIsPickImage = false;
            }
            else{
                uploadImages();
            }
        }

    }

    @Override
    public void onUpdateProductInformation(boolean status) {

    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {

    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onDeleteFile(boolean status) {
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(!status){
            ((NewPostActivity)getActivity()).toastHelper.toast("Xóa không thành công. Vui lòng thử lại sau", ToastHelper.LENGTH_SHORT);
        }
        else{
            ARR_PHOTO.remove(mPosition);
            IMAGE_ADAPTER.notifyDataSetChanged();
            ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        }
    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public void onRequireDeleteFile(int position) {
        ((NewPostActivity)getActivity()).dialogHelper.show();
        new PresenterNewPost(this).handleDeleteFile(ARR_PHOTO.get(position).getPhotoLink());
        mPosition = position;
    }

    private int checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED){
                return  0;
            }
            else{
                return  1;
            }
        }
        else{
            return 1;
        }
    }

    private void requestPermissions(int requestCode){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), 1001);
            }
        }
        else if(requestCode == 102){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 101);
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroyView() {
        //clear memory
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }
}
