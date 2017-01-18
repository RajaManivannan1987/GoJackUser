package com.example.im028.gojackuser.Utility.ConstantClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.Settings;
import android.widget.EditText;
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

}
