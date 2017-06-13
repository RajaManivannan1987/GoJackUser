package com.calljack.im028.calljack.ApplicationClass;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.calljack.im028.calljack.GCMClass.RegistrationIntentService;
import com.calljack.im028.calljack.MapIntegrationClasses.MyLocation;
import com.calljack.im028.calljack.Utility.InterNet.ConnectivityReceiver;
import com.calljack.im028.calljack.Utility.font.FontsOverride;

/**
 * Created by IM028 on 8/3/16.
 */
public class MyApplication extends MultiDexApplication {
    private static MyLocation location;
    private static MyApplication myApplication;
    private RequestQueue mRequestQueue;

    public static MyLocation locationInstance() {
        return location;
    }

    public static void instanceLocation(Context context) {
        location = new MyLocation(context);
    }


    public MyApplication() {

    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/roboto_regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/rupee_foradian.ttf");
        startService(new Intent(this, RegistrationIntentService.class));
        myApplication = this;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addRequest(Request request) {
        getRequestQueue().add(request);
    }
}
