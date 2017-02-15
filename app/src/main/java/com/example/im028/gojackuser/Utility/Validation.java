package com.example.im028.gojackuser.Utility;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IM028 on 8/2/16.
 */
public class Validation {
    public static String userNameError = "Enter valid EmailId";
    public static String passwordError = "Password must be minimum 6 character";
    public static String mobileNoError = "Enter 10 digit mobile number or EmailId";
    public static String otpError = "Enter Valid OTP";
    public static String userNameMobileNoError = "Enter 10 digit mobile no";

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

    public static String emailPhoneValidation(String string) {
        if (!TextUtils.isEmpty(string)) {
            if (Patterns.EMAIL_ADDRESS.matcher(string).matches()) {
                return "email";
            } else if (isValidPhone(string)) {
                return "phone";
            }
            return onlyContainsNumbers(string);
        } else {
            return mobileNoError;
        }
    }

    private static boolean isValidPhone(String phone) {

        String PHONE_PATTERN = "\\+?\\d[\\d -]{8,12}\\d";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private static String onlyContainsNumbers(String text) {
        try {
            Long.parseLong(text);
            return userNameMobileNoError;
        } catch (NumberFormatException ex) {
            return userNameError;
        }
    }

}
