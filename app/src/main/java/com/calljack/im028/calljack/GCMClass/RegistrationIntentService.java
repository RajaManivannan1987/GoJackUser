package com.calljack.im028.calljack.GCMClass;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 4/20/16.
 */

public class RegistrationIntentService extends IntentService {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("490936590175",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            if (new Session(this, TAG).getIsLogin()) {
                new WebServices(this, TAG).saveDeviceId(token, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {

                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }


        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }


}
