package com.qtctek.aladin.helper;

import android.app.Activity;
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

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void hideKeyboard(final View view){
        InputMethodManager keyboard=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
