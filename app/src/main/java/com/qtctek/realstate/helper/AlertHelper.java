package com.qtctek.realstate.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

public class AlertHelper {

    public static final int ALERT_NO_ACTION = 1002;

    public interface AlertHelperCallback{
        void onPositiveButtonClick(int option);
        void onNegativeButtonClick(int option);
    }

    private Context mContext;
    private AlertHelperCallback callback;
    

    public void setCallback(AlertHelperCallback callback) {
        this.callback = callback;
    }

    public AlertHelper(Context mContext) {
        this.mContext = mContext;
    }
    

    public void alert(String title, String message, boolean cancelAble){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setCancelable(cancelAble)
                .setMessage(message);
        mBuilder.show();
    }

    public void alert(@StringRes int title, @StringRes int message, boolean cancelAble){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(title))
                .setCancelable(cancelAble)
                .setMessage(mContext.getResources().getString(message));
        mBuilder.show();
    }

    public void alert(String title, String message, boolean cancelAble, String positiveButtonText,
                      final int option){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setCancelable(cancelAble)
                .setMessage(message);
        mBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onPositiveButtonClick(option);
                }
                dialog.dismiss();
            }
        });
        mBuilder.show();

    }

    public void alert(@StringRes int title, @StringRes int message, boolean cancelAble, @StringRes int positiveButtonText,
                      final int option){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(title))
                .setCancelable(cancelAble)
                .setMessage(mContext.getResources().getString(message));
        mBuilder.setPositiveButton(mContext.getResources().getString(positiveButtonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onPositiveButtonClick(option);
                }
                dialog.dismiss();
            }
        });
        mBuilder.show();

    }

    public void alert(String title, String message, boolean cancelAble, String positiveButtonText,
                      String negativeButtonText, final int option){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setCancelable(cancelAble)
                .setMessage(message);
        mBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onPositiveButtonClick(option);
                }
                dialog.dismiss();
            }
        });
        mBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onNegativeButtonClick(option);
                }
                dialog.dismiss();
            }
        });

        mBuilder.show();
    }

    public void alert(@StringRes int title, @StringRes int message, boolean cancelAble, @StringRes int positiveButtonText,
                      @StringRes int negativeButtonText, final int option){
        if(mContext == null){return;}

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(title))
                .setCancelable(cancelAble)
                .setMessage(mContext.getResources().getString(message));
        mBuilder.setPositiveButton(mContext.getResources().getString(positiveButtonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onPositiveButtonClick(option);
                }
                dialog.dismiss();
            }
        });
        mBuilder.setNegativeButton(mContext.getResources().getString(negativeButtonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onNegativeButtonClick(option);
                }
                dialog.dismiss();
            }
        });

        mBuilder.show();
    }


}
