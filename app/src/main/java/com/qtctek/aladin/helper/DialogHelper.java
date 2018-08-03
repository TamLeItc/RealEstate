package com.qtctek.aladin.helper;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.Window;

import com.qtctek.aladin.R;


public class DialogHelper {

    private Dialog mLoadingDialog;
    private Context mContext;
    private int layoutCustom;

    public DialogHelper(Context context) {
        this.mContext = context;
        createLoadingDialog();
    }


    public DialogHelper(Context context, @LayoutRes int layout) {
        this.mContext = context;
        layoutCustom = layout;
        createLoadingDialog();
    }

    public void createLoadingDialog() {
        this.mLoadingDialog = new Dialog(mContext);
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layoutCustom > 0)
            this.mLoadingDialog.setContentView(layoutCustom);
        else{
            mLoadingDialog.setContentView(R.layout.dialog_loading);
        }
        this.mLoadingDialog.setCancelable(false);
    }

    public void show() {
        if (mLoadingDialog == null) {
            createLoadingDialog();
        }
        mLoadingDialog.show();
    }

    public void dismiss() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
