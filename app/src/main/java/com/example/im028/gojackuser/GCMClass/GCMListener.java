package com.example.im028.gojackuser.GCMClass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.im028.gojackuser.ActivityClasses.LocationCheckActivity;
import com.example.im028.gojackuser.ActivityClasses.RideActivity;
import com.example.im028.gojackuser.ActivityClasses.SplashActivity;

import com.example.im028.gojackuser.DialogFragment.GenderRequestDialogActivity;
import com.example.im028.gojackuser.DialogFragment.PilotHereDialogActivity;
import com.example.im028.gojackuser.DialogFragment.TripCompletedDialogActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Singleton.ActionCompletedSingleton;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.CompletedInterface;
import com.example.im028.gojackuser.Utility.Session;
import com.google.android.gms.gcm.GcmListenerService;


import java.util.Calendar;

/**
 * Created by IM028 on 4/20/16.
 */

public class GCMListener extends GcmListenerService {

    private static final String TAG = "GCMListener";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String type = data.getString("type", "");
        String message = data.getString("message", "");
        String gender = data.getString("gender", "");
        String requestid = data.getString("requestid", "");
        String ridetype = data.getString("ridetype", "");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "type: " + type);
        Log.d(TAG, "gender: " + gender);
        Log.d(TAG, "requestid: " + requestid);
        Log.d(TAG, "rideType:" + ridetype);
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (new Session(this, "GCM").getIsLogin()) {
            sendNotification(message, type, gender, requestid, ridetype);
        }


    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String type, String gender, String requestid, String ridetype) {
        Intent intent = null;
        switch (type) {
            case "driveraccepted":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                break;
            case "drivercancelled":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                break;
            case "pilotreached":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                startActivity(new Intent(this, PilotHereDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case "tripstarted":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                break;
            case "tripcompleted":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                //startActivity(new Intent(this, TripCompletedDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case "gendernotification":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                ConstantFunctions.updateRide(this);
                startActivity(new Intent(this, GenderRequestDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(ConstantValues.genderType, gender).putExtra(ConstantValues.requestid, requestid).putExtra(ConstantValues.message, message));
                break;
            case "tripnotaccepted":
                intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, ridetype);
                startActivity(new Intent(this, TripCompletedDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                ActionCompletedSingleton.getNopilot().setListener(new CompletedInterface() {
                    @Override
                    public void completed() {
                        Intent intent = new Intent(ConstantValues.driverStatus);
                        sendBroadcast(intent);
                    }
                });
                break;
            default:
                intent = new Intent(this, SplashActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.male_pilot_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), notificationBuilder.build());
    }

}
