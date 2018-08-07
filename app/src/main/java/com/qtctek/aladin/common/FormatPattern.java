package com.qtctek.aladin.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatPattern {

    public static boolean checkEmail(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public  static boolean checkNumberPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        }
        else{
            if(number.length() != 10 && number.length() != 11){
                return false;
            }
            return true;
        }
    }

    public  static boolean checkUserName(String username) {
        Pattern pattern = Pattern.compile("\\b[a-zA-Z][a-zA-Z0-9]{3,}\\b");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return false;
        }
        else{
            return true;
        }
    }

}
