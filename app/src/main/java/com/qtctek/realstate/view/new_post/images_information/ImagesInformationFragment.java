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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.interfaces.OnRequireHandleFromAdapter;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ImagesInformationFragment extends Fragment implements ViewHandleModelNewPost, OnRequireHandleFromAdapter,
        View.OnClickListener {

    private View mView;

    private Button mBtnSelectImage;
    private Button mBtnSaveImage;
    private Button mBtnSelectAvartar;
    private Button mBtnSaveAvartar;
    private Button mBtnNext;
    private RecyclerView mRecyclerView;
    private Dialog mLoadingDialog;
    private ImageView mImvAvartar;

    public static ArrayList<Uri> ARR_URI = new ArrayList<>();
    public static ImageAdapter IMAGE_ADAPTER;
    public static int QUALITY_IMAGE = 0;
    public static int QUALITY_IMAGE_UPLOADED = 0;
    public static int FILE_NAME = 1;

    private int mPosition = 0;
    private boolean mIsPickAvartar = false;
    private boolean mIsPickImage = false;
    private boolean mIsHandleUploadAvartar = false;
    private String mPath;
    private Uri mUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_images_information, container, false);

        createLoadingDialog();
        initViews();
        handleStart();
        handleRecyclerView();

        return mView;
    }

    private void initViews() {
        this.mBtnSelectImage = mView.findViewById(R.id.btn_select_image);
        this.mBtnSelectAvartar = mView.findViewById(R.id.btn_select_avartar);
        this.mBtnSaveImage = mView.findViewById(R.id.btn_save_image);
        this.mBtnSaveAvartar = mView.findViewById(R.id.btn_save_avartar);
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mRecyclerView = mView.findViewById(R.id.recycler_view);
        mImvAvartar = mView.findViewById(R.id.imv_avartar);

        this.mBtnSelectImage.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mBtnSelectAvartar.setOnClickListener(this);
        this.mBtnSaveImage.setOnClickListener(this);
        this.mBtnSaveAvartar.setOnClickListener(this);
    }

    private void handleStart(){
        String url = MainActivity.HOST + "/real_estate/images/" + NewPostActivity.POST_SALE.getId() + "_avartar.jpg";
        Picasso.with(getContext()).load(url).into(mImvAvartar);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleRecyclerView() {
        ARR_URI.clear();
        this.IMAGE_ADAPTER = new ImageAdapter(getContext(), ARR_URI, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerView.setLayoutManager(layoutManager);

        this.mRecyclerView.setAdapter(IMAGE_ADAPTER);
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
            this.mImvAvartar.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadAvartar(){

        mLoadingDialog.show();

        mPath = getRealPathFromURI(getContext(), mUri);

        String fileName = NewPostActivity.POST_SALE.getId() + "_avartar.jpg";
        new PresenterNewPost(this).handlePostImage(NewPostActivity.POST_SALE.getId(),
                fileName, mPath);
    }

    private void handleResultImage(Intent data) {
        ClipData mClipData = data.getClipData();
        for (int i = 0; i < mClipData.getItemCount(); i++) {

            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            ARR_URI.add(uri);
        }
        QUALITY_IMAGE = ARR_URI.size();
        this.IMAGE_ADAPTER.notifyDataSetChanged();
    }

    private void uploadImages() {
        Uri uri = ARR_URI.get(QUALITY_IMAGE_UPLOADED);
        String path = getRealPathFromURI(getContext(), uri);

        String fileName = NewPostActivity.POST_SALE.getId() + "_" + (FILE_NAME++) + ".jpg";
        new PresenterNewPost(this).handlePostImage(NewPostActivity.POST_SALE.getId(),
                fileName, path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            mIsPickImage = true;
            if (data.getData() != null) {
                Uri uri = data.getData();
                ARR_URI.add(uri);
                QUALITY_IMAGE += 1;
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
            case R.id.btn_save_image:
                if (QUALITY_IMAGE != 0 && QUALITY_IMAGE > QUALITY_IMAGE_UPLOADED && mIsPickImage) {
                    mLoadingDialog.show();
                    mIsHandleUploadAvartar = false;
                    mIsPickImage = false;
                    uploadImages();
                }
                break;
            case R.id.btn_save_avartar:
                if(mIsPickAvartar){
                    mLoadingDialog.show();
                    mIsHandleUploadAvartar = true;
                    mIsPickAvartar = false;
                    uploadAvartar();
                }
                break;
            case R.id.btn_next_to:
                handleNextTo();
                break;
        }
    }

    private void handleNextTo(){
        String message = "";
        if(mIsPickAvartar){
            message = " ảnh đại diện ";
        }
        if(mIsPickImage){
            if(message.equals("")){
                message = " ảnh chi tiết ";
            }
            else{
                message += " và ảnh chi tiết ";
            }
        }
        if(mIsPickImage || mIsPickAvartar){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setMessage("Bạn chưa lưu" + message + ". Bạn có muốn tiếp tục không lưu!")
                    .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                            viewPager.setCurrentItem(1);
                        }
                    })
                    .setPositiveButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
        else{
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

    }

    @Override
    public void onUploadImages(boolean status) {
        if(mIsHandleUploadAvartar){
            mLoadingDialog.dismiss();
            if (!status) {
                Toast.makeText(getContext(), "Xảy ra lỗi trong việc lưu ảnh chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            QUALITY_IMAGE_UPLOADED += 1;
            if(QUALITY_IMAGE == QUALITY_IMAGE_UPLOADED){
                mLoadingDialog.dismiss();
                if (!status) {
                    Toast.makeText(getContext(), "Xảy ra lỗi trong việc lưu ảnh chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                uploadImages();
            }
        }

    }

    @Override
    public void onUpdateNormalInformation(boolean status) {

    }

    @Override
    public void onUpdateMoreInformation(boolean status) {

    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {

    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onUpdateContactInformation(boolean status) {

    }

    @Override
    public void onDeleteFile(boolean status) {
        mLoadingDialog.dismiss();
        if(!status){
            Toast.makeText(getContext(), "Xóa không thành công. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
        }
        else{
            ARR_URI.remove(mPosition);
            IMAGE_ADAPTER.notifyDataSetChanged();
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public void onRequireDeleteFile(int position) {
        mLoadingDialog.show();
        new PresenterNewPost(this).handleDeleteFile(ARR_URI.get(position).toString());
        mPosition = position;
        QUALITY_IMAGE -= 1;
        QUALITY_IMAGE_UPLOADED -= 1;
    }

    private int checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED){
                if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    return 0;
                }
                else{
                    return  -1;
                }
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
    }
}
