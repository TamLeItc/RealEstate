package com.qtctek.aladin.view.new_post.images_information;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.Photo;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.new_post.PresenterNewPost;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.new_post.interfaces.OnRequireHandleFromAdapter;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ImagesInformationFragment extends Fragment implements ViewHandleModelNewPost, OnRequireHandleFromAdapter,
        View.OnClickListener {

    private NewPostActivity mActivity;

    private View mView;

    public RelativeLayout rlViewLarge;
    public ImageView imvViewLarge;
    private ImageView mImvCancelViewLarge;
    private TextView mTxvSelectImage;
    private TextView mTxvSelectAvartar;
    private Button mBtnNext;
    public ProgressBar progressBarLoading;
    private RecyclerView mRecyclerView;
    public ImageView imvAvartar;

    public ImageAdapter imageAdapter;
    public ArrayList<Photo> arrPhoto = new ArrayList<>();
    public int fileName = 1;

    public boolean isSaveTemp;
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

        mActivity = (NewPostActivity) getActivity();

        initViews();
        handleStart();

        return mView;
    }

    private void initViews() {
        this.mTxvSelectImage = mView.findViewById(R.id.txv_select_image);
        this.mTxvSelectAvartar = mView.findViewById(R.id.txv_select_avartar);
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mRecyclerView = mView.findViewById(R.id.recycler_view);
        imvAvartar = mView.findViewById(R.id.imv_avartar);
        progressBarLoading = mView.findViewById(R.id.progress_bar);
        this.rlViewLarge = mView.findViewById(R.id.rl_view_large);
        this.mImvCancelViewLarge = mView.findViewById(R.id.imv_cancel_view_large);
        this.imvViewLarge = mView.findViewById(R.id.imv_view_large);

        this.mImvCancelViewLarge.setOnClickListener(this);
        this.mTxvSelectImage.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mTxvSelectAvartar.setOnClickListener(this);
    }

    private void handleStart() {
        imageAdapter = new ImageAdapter(mActivity, arrPhoto, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerView.setLayoutManager(layoutManager);

        this.mRecyclerView.setAdapter(imageAdapter);

        try {
            String url = MainActivity.IMAGE_URL + mActivity.product.getThumbnail();
            progressBarLoading.setVisibility(View.VISIBLE);
            Picasso.with(mActivity).load(url).into(imvAvartar, new Callback() {
                @Override
                public void onSuccess() {
                    progressBarLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressBarLoading.setVisibility(View.GONE);
                    imvAvartar.setImageResource(R.drawable.icon_product);
                }
            });
        } catch (java.lang.NullPointerException ignored) {
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return "";
        }
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        if (cursor == null) {
            return "";
        }

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void handleResultAvartar() {
        try {
            InputStream inputStream = mActivity.getContentResolver().openInputStream(mUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imvAvartar.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void handleResultImage(Intent data) {
        ClipData mClipData = data.getClipData();
        for (int i = 0; i < mClipData.getItemCount(); i++) {

            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            arrPhoto.add(new Photo(uri));
        }
        imageAdapter.notifyDataSetChanged();
    }

    private void uploadAvartar() {
        mActivity.getDialogHelper().show();

        mPath = getRealPathFromURI(mActivity, mUri);

        String fileName = mActivity.product.getId() + Product.IMAGE_NAME;
        new PresenterNewPost(this).handlePostImage(mActivity.product.getId(),
                fileName, mPath, Product.THUMBNAIL);
    }

    private void uploadImages() {
        while (!arrPhoto.get(mQualityImageUploaded).getIsUpload()) {
            mQualityImageUploaded++;
            if (mQualityImageUploaded > arrPhoto.size() - 1) {
                return;
            }
        }
        Uri uri = arrPhoto.get(mQualityImageUploaded).getUri();
        String path = getRealPathFromURI(mActivity, uri);

        String fileNameImage = mActivity.product.getId() + "_" + (this.fileName++) + Constant.IMAGE_EXTENSION;
        new PresenterNewPost(this).handlePostImage(mActivity.product.getId(),
                fileNameImage, path, Product.IMAGE_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            mIsPickImage = true;
            if (data.getData() != null) {
                Uri uri = data.getData();
                arrPhoto.add(new Photo(uri));
                this.imageAdapter.notifyDataSetChanged();
            } else if (data.getClipData() != null) {
                handleResultImage(data);
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            this.mUri = data.getData();
            this.mIsPickAvartar = true;

            handleResultAvartar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSelectImages() {
        int check = checkPermission();
        if (check == 1) {
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), 101);
        } else if (check == 0) {
            requestPermissions(101);
        }
    }

    private void handleSelectAvartar() {
        int check = checkPermission();
        if (check == 1) {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1, 102);
        } else if (check == 0) {
            requestPermissions(102);
        }
    }

    private void handelCancelViewLarge() {

        mActivity.toolbar.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.zoom_100_to_0);
        this.rlViewLarge.startAnimation(animation);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_select_image:
                handleSelectImages();
                break;
            case R.id.txv_select_avartar:
                handleSelectAvartar();
                break;
            case R.id.btn_next_to:
                this.isSaveTemp = false;
                handleSaveImageInformation();
                break;
            case R.id.imv_cancel_view_large:
                handelCancelViewLarge();
                break;
        }
    }

    public void handleSaveImageInformation() {
        mQualityImageUploaded = 0;
        if (mIsPickImage && !mIsPickAvartar) {
            if (mIsPickImage) {
                mActivity.getDialogHelper().show();
                uploadImages();
            }
        } else if (mIsPickAvartar) {
            mActivity.getDialogHelper().show();
            uploadAvartar();
        } else {
            if (!isSaveTemp) {
                ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
            }
        }
    }

    public boolean checkSavedInformation() {
        if (mIsPickImage || mIsPickAvartar) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

    }

    @Override
    public void onUploadImages(boolean status) {

        if (!status) {
            mActivity.getDialogHelper().dismiss();
            mActivity.getToastHelper().toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
        }
        else {
            mActivity.getDialogHelper().dismiss();

            if (mIsPickAvartar) {


                mIsPickAvartar = false;

                if (mIsPickImage) {
                    uploadImages();
                } else {
                    if (!isSaveTemp) {
                        ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(1);
                    } else {
                        mActivity.getToastHelper().toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
                    }

                }

            } else {
                mQualityImageUploaded += 1;
                if (arrPhoto.size() == mQualityImageUploaded) {
                    mActivity.getDialogHelper().dismiss();
                    if (!status) {
                        mActivity.getToastHelper().toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
                    } else {
                        if (!isSaveTemp) {
                            ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                            viewPager.setCurrentItem(1);
                        } else {
                            mActivity.getToastHelper().toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
                        }
                    }
                    mIsPickImage = false;
                } else {
                    uploadImages();
                }
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
        mActivity.getDialogHelper().dismiss();
        if (!status) {
            mActivity.getToastHelper().toast(R.string.delete_failed, ToastHelper.LENGTH_SHORT);
        } else {
            arrPhoto.remove(mPosition);
            imageAdapter.notifyDataSetChanged();
            mActivity.getDialogHelper().dismiss();
        }
    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public void onRequireDeleteFile(int position) {
        mActivity.getDialogHelper().show();
        new PresenterNewPost(this).handleDeleteFile(arrPhoto.get(position).getPhotoLink(), mActivity.product.getId());
        mPosition = position;
    }

    private int checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    private void requestPermissions(int requestCode) {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), 1001);
            }
        } else if (requestCode == 102) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 101);
            }
        } else {
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
