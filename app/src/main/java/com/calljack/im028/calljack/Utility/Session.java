package com.calljack.im028.calljack.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IM028 on 8/2/16.
 */
public class Session {
    private String TAG;
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private static String PREF_NAME = "go_jack_rider";
    private static String customerId = "customerId";
    private static String name = "name";
    private static String token = "token";
    private static final String IS_LOGIN = "IsLoggedIn";


    public Session(Context context, String TAG) {
        this.context = context;
        this.TAG = "Session " + TAG;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String customerId, String name, String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(this.customerId, customerId);
        editor.putString(this.name, name);
        editor.putString(this.token, token);
        editor.commit();
    }

    public String getCustomerId() {
        return pref.getString(customerId, "");
    }

    public String getToken() {
        return pref.getString(token, "");
    }

    public String getName() {
        return pref.getString(name, "");
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public boolean getIsLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
