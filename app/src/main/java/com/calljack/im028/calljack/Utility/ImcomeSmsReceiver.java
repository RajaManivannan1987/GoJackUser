package com.calljack.im028.calljack.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.calljack.im028.calljack.Utility.InterfaceClasses.InComeSms;


/**
 * Created by Im033 on 12/20/2016.
 */

public class ImcomeSmsReceiver extends BroadcastReceiver {
    private String TAG = ImcomeSmsReceiver.class.getName();
    private static InComeSms imComeSms;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String PhNo = currentMessage.getDisplayOriginatingAddress();
                    String[] senderName = new String[]{PhNo};
                    String[] senderId = PhNo.split("-");
                    String id = senderId[1];
                    String message = currentMessage.getDisplayMessageBody();
                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + id);
                    try {
                        if (id.equals("SHOUTJ")){
                            imComeSms.messageReceived(message);
                        }
                    }catch (Exception e){

                    }

                }
            }
        } catch (Exception e) {

        }

    }

    public static void bindMessageListener(InComeSms listener) {
        imComeSms = listener;
    }
}
