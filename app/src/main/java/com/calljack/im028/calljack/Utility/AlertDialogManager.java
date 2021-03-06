package com.calljack.im028.calljack.Utility;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.calljack.im028.calljack.ActivityClasses.LoginActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.DialogBoxInterface;


/**
 * class for alert box
 */

public class AlertDialogManager {


    /**
     * @param context context of activity
     * @param title   alert box title
     * @param message alert box message
     * @param status  alert box icon boolean
     */
    public static void showAlertDialog(final Context context, String title, String message,
                                       Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.wrong);

        // Setting OK Button

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

            }
        });
        // Showing Alert Message
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param activity context activity
     * @param title    alert box title
     * @param message  alert box message
     * @return
     */
    public static void alertBox(final Context activity, String title, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                activity.startActivity(new Intent(activity, LoginActivity.class));

            }
        });
        // Showing Alert Message
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void listenerDialogBox(Context context, String title, String message, @Nullable final DialogBoxInterface listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.yes();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.no();
                dialog.dismiss();
            }
        });
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void regenerateOTP(Context context, String title, String message, @Nullable final DialogBoxInterface listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Regenerate OTP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.yes();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.no();
                dialog.dismiss();
            }
        });
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenerDialogBox(Context context, String title, String message, String positive, String negative, @Nullable final DialogBoxInterface listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.yes();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.no();
                dialog.dismiss();
            }
        });
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


