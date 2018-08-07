package com.qtctek.aladin.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.qtctek.aladin.dto.Room;
import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AppUtils {
    public static String USERNAME_HEADER = "tam.le";
    public static String PASSWORD_HEADER = "tamle@123";
    public static String AUTHORIZATION_HEADER = "Basic dGFtLmxlOnRhbWxlQDEyMw==";

    public static String USERNAME = "Username";
    public static String PASSWORD = "Password";
    public static String AUTHORIZATION = "Authorization";

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

    public static void exit(Context context, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        System.exit(1);
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

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

}
