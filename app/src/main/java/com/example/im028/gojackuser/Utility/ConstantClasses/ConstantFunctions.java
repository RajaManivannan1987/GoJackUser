package com.example.im028.gojackuser.Utility.ConstantClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im028.gojackuser.ActivityClasses.LoginActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * Created by IM028 on 8/2/16.
 */
public class ConstantFunctions {
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Their is no call Feature", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sms(Context context, String phoneNumber, String messageText) {
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", messageText);
        context.startActivity(Intent.createChooser(smsIntent, "Message to pilot"));
    }

    public static void logout(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public static String getMarkerMovedAddress(Context context, LatLng dragPosition) {
        String adres = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnedAddress.getAddressLine(i)).append(",");
                }
                adres = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            adres = "Map Pointed";
        }
        return adres;
    }

    public static String getMarkerMovedAddress(Context context, LatLng dragPosition, EditText editText) {
        String adres = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnedAddress.getAddressLine(i)).append(",");
                }
                adres = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            adres = "Pointer Location";
        }
        editText.setText(adres);
        return adres;
    }

    public static void updateRide(Context context) {
        Intent intent = new Intent(ConstantValues.driverStatus);
        context.sendBroadcast(intent);
    }

    public static boolean checkmarshmallowPermission(Activity activity, String permision, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, permision) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permision}, requestCode);
            return true;
        }
        return false;
    }

    public static float getDistance(LatLng pickUpLatLng, LatLng deliverLatLng) {
        Location locationA = new Location("LocationA");
        locationA.setLatitude(pickUpLatLng.latitude);
        locationA.setLongitude(pickUpLatLng.longitude);
        Location locationB = new Location("LocationB");
        locationB.setLatitude(deliverLatLng.latitude);
        locationB.setLongitude(deliverLatLng.longitude);
        return locationA.distanceTo(locationB);
    }

    public boolean isOnline(Context act) {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showSnakBar(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
    public static void hideKeyboard(Context context,View v) {
        InputMethodManager input = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
