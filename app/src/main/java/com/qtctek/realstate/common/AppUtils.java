package com.qtctek.realstate.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.qtctek.realstate.dto.Room;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AppUtils {

    public static int NOTIFICATION = 1;

    public static String USERNAME = "user_name";
    public static String PASSWORD = "password";

    public static String LONG_NEGOTIATE = "Thương lượng";
    public static String SHORT_NEGOTIATE = "TL";

    public static String QUALITY_BATHROOM = "quality_bathroom";
    public static String QUALITY_BEDROOM = "quality_bedroom";
    public static int SHORT_PRICE = 1;
    public static int LONG_PRICE = 2;

    public static int REQUEST_CODE_PERMISSION = 1001;

    public static int QUALITY_ELEMENT = 6;

    public static ArrayList<Room> getArrListQuality(){
        ArrayList<Room> arrayList = new ArrayList<>();
        for(int i = 0; i < QUALITY_ELEMENT; i++){
            arrayList.add(new Room(i, false));
        }
        return arrayList;
    }



    public static String getStringPrice(Long price, int option){
        if(option == SHORT_PRICE){
            float fPrice;
            if(price > 1000000000){
                fPrice = (float)price / 1000000000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " tỉ";
                }
                else{
                    return fPrice + " tỉ";
                }
            }
            else if(price > 1000000){
                fPrice = (float)price / 1000000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " tr";
                }
                else{
                    return fPrice + " tr";
                }
            }
            else if(price > 1000){
                fPrice = (float)price / 1000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " K";
                }
                else{
                    return fPrice + " K";
                }
            }
            else{
                return SHORT_NEGOTIATE;
            }
        }
        else{
            float fPrice;
            if(price > 1000000000){
                fPrice = (float)price / 1000000000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " tỉ";
                }
                else{
                    return fPrice + " tỉ";
                }
            }
            else if(price > 1000000){
                fPrice = (float)price / 1000000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " triệu";
                }
                else{
                    return fPrice + " triệu";
                }
            }
            else if(price > 1000){
                fPrice = (float)price / 1000;
                fPrice = (float) (Math.round(fPrice * 100.0)/100.0);
                if(fPrice == (int)fPrice){
                    return (int)fPrice + " K";
                }
                else{
                    return fPrice + " K";
                }
            }
            else{
                return LONG_NEGOTIATE;
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

}
