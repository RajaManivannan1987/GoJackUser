package com.example.im028.gojackuser.ActivityClasses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterNet.ConnectivityReceiver;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.ScheduleThread.ScheduleThread;
import com.example.im028.gojackuser.Utility.ScheduleThread.TimerInterface;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationCheckActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static String TAG = "LocationCheckActivity";
    private LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private final int MY_LOCATION = 1;
    private ScheduleThread thread;
    private WebServices webServices;
    private String type;
    private RelativeLayout locationCheckRelativeLayout;

    public static Intent getLocationCheck(Activity activity, String type) {
        return new Intent(activity, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, type);
        //   ride or courier
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check);
        webServices = new WebServices(this, TAG);
        locationCheckRelativeLayout = (RelativeLayout) findViewById(R.id.locationCheckRelativeLayout);
        type = getIntent().getExtras().getString(ConstantValues.rideTypeRide);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        thread = new ScheduleThread(new TimerInterface() {
            @Override
            public void onRun() {
                Log.d(TAG, "Inside Thread");
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    Log.d(TAG, "Inside Thread Success");
                    webServices.getActiveRide(type, new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (!response.getString("status").equalsIgnoreCase("0") && response.getJSONArray("data").length() > 0) {
                                if (type.equalsIgnoreCase(ConstantValues.rideTypeRide)) {
                                    startActivity(new Intent(LocationCheckActivity.this, RideActivity.class).putExtra(ConstantValues.rideId, response.getJSONArray("data").getJSONObject(0).getString("ride_id")));
                                } else {
                                    startActivity(new Intent(LocationCheckActivity.this, ListCourierActivity.class));
                                }
                            } else {
                                if (type.equalsIgnoreCase(ConstantValues.rideTypeRide)) {
                                    startActivity(new Intent(LocationCheckActivity.this, DashboardActivity.class));
                                } else {
                                    startActivity(new Intent(LocationCheckActivity.this, CourierActivity.class));
                                }
                            }
                            finish();
                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });
                    thread.stop();
                }
            }
        }, 2000, 0, this);
        thread.start();
    }

    private void getStatusRide() {
        webServices.checkStatus(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("2"))
                    startActivity(new Intent(LocationCheckActivity.this, DashboardActivity.class));
                else if (response.getString("status").equalsIgnoreCase("4")) {
                    startActivity(new Intent(LocationCheckActivity.this, TripDetailsActivity.class).putExtra("data", response.getJSONObject("data").toString()));
                } else
                    startActivity(new Intent(LocationCheckActivity.this, RideActivity.class));
                finish();
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(LocationCheckActivity.this, title, message, false);
            }
        });

        thread.stop();
    }

    private void enableLocation() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Enable Location Services");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        } else {
            enableMyLocation();
        }
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationCheckActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION);
        } else {
            MyApplication.instanceLocation(LocationCheckActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    ConstantFunctions.toast(LocationCheckActivity.this, "My Location permission denied");
                    finish();
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocation();
        MyApplication.getInstance().setConnectivityListener(this);
//        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notifManager.cancelAll();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = null;
        int color = 0;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        } else {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        }
        Snackbar snackbar = Snackbar.make(locationCheckRelativeLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
