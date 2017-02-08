package com.example.im028.gojackuser.Utility;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by IM028 on 8/2/16.
 */
public class Validation {
    public static String userNameError = "User Name must be greater than 6";
    public static String passwordError = "password must be greater than 6";
    public static String mobileNoError = "Enter 10 digit mobile number";
    public static String otpError = "Enter Valid Otp";

    public static boolean isPasswordValid(String text) {
        return text.length() >= 6;
    }

    public static boolean isUserNameValid(String text) {
        return !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
    public static boolean isUserNameValid1(String text) {
        return !TextUtils.isEmpty(text);
    }

    public static boolean isMobileNumberValid(String text) {
        return !TextUtils.isEmpty(text) && Patterns.PHONE.matcher(text).matches();
    }
    public static boolean isOtpValid(String text) {
        return !TextUtils.isEmpty(text) && text.length() >= 4;
    }

}
