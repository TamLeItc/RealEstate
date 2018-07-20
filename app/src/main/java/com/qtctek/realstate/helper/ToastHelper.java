package com.qtctek.realstate.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {

    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

    private Context mContext;

    public ToastHelper(Context context) {

        mContext = context;
    }

    public void toast(String message, int duration){
        Toast.makeText(mContext, message, duration).show();
    }
}
