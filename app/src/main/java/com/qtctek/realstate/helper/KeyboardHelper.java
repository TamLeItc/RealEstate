package com.qtctek.realstate.helper;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardHelper {

    private Context mContext;

    public KeyboardHelper(Context context){
        this.mContext = context;
    }

    public void showKeyboard(final View view){
        view.requestFocus();
        InputMethodManager keyboard=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view,0);
    }

    public void hideKeyboard(final View view){
        InputMethodManager keyboard=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
