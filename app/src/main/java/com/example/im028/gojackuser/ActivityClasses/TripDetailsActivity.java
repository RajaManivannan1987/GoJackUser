package com.example.im028.gojackuser.ActivityClasses;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class TripDetailsActivity extends BackCommonActivity {
    private static String TAG = "TripDetailsActivity";
    private ImageView pilotImageView;
    private TextView totalAmountTextView, nameTextView;
    private RatingBar helmetRatingBar, faceRatingBar, overAllRatingBar;
    private RadioButton petrolYesRadioButton, petrolNoRadioButton, rudeYesRadioButton, rudeNoRadioButton, helmatYesRadioButton, helmatNoRadioButton, faceMaskYesRadioButton, faceMaskNoRadioButton;
    private Button submitButton;
    private JSONObject jsonObject;
    private NotificationManager nMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_trip_details);
        nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            jsonObject = new JSONObject(getIntent().getExtras().getString("data"));
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }


        pilotImageView = (ImageView) findViewById(R.id.tripDetailsActivityImageView);
        totalAmountTextView = (TextView) findViewById(R.id.tripDetailsActivityAmountTextView);
        nameTextView = (TextView) findViewById(R.id.tripDetailsActivityNameTextView);
        helmetRatingBar = (RatingBar) findViewById(R.id.tripDetailsActivityHelmetRatingBar);
        faceRatingBar = (RatingBar) findViewById(R.id.tripDetailsActivityFaceMaskRatingBar);
        overAllRatingBar = (RatingBar) findViewById(R.id.tripDetailsActivityOverAllRatingBar);
        petrolYesRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityPetrolYesRadioButton);
        petrolNoRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityPetrolNoRadioButton);
        rudeYesRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityRudeYesRadioButton);
        rudeNoRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityRudeNoRadioButton);
        helmatYesRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityHelmatYesRadioButton);
        helmatNoRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityHelmatNoRadioButton);
        faceMaskYesRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityFaceMaskYesRadioButton);
        faceMaskNoRadioButton = (RadioButton) findViewById(R.id.tripDetailsActivityFaceMaskNoRadioButton);
        submitButton = (Button) findViewById(R.id.tripDetailsActivitySubmitButton);

        try {
            totalAmountTextView.setText(jsonObject.getString("cash"));
            nameTextView.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMgr.cancelAll();
                //   By raja

               /* if (helmetRatingBar.getRating() == 0) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Rate Helmet");
                    return;
                }
                if (faceRatingBar.getRating() == 0) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Rate Face Mask");
                    return;
                }*/
                if (overAllRatingBar.getRating() == 0) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Rate Ride");
                    return;
                }
                if (!(helmatYesRadioButton.isChecked() || helmatNoRadioButton.isChecked())) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Select helmat provide or not");
                    return;
                }
                if (!(faceMaskYesRadioButton.isChecked() || faceMaskNoRadioButton.isChecked())) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "provide facemask or not");
                    return;
                }
                if (!(petrolYesRadioButton.isChecked() || petrolNoRadioButton.isChecked())) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Select petrol station or not");
                    return;
                }
                if (!(rudeYesRadioButton.isChecked() || rudeNoRadioButton.isChecked())) {
                    ConstantFunctions.toast(TripDetailsActivity.this, "Select rude or not");
                    return;
                }

                try {
                    new WebServices(TripDetailsActivity.this, TAG).saveTripDetails(
                            jsonObject.getString("rideid"),
                            jsonObject.getString("driverid"),
                            //   By raja
//                            (int) helmetRatingBar.getRating() + "",
//                            (int) faceRatingBar.getRating() + "",
                            helmatYesRadioButton.isChecked() ? "1" : "5",
                            faceMaskYesRadioButton.isChecked() ? "1" : "5",
                            petrolYesRadioButton.isChecked() ? "1" : "5",
                            rudeYesRadioButton.isChecked() ? "1" : "5",
                            (int) overAllRatingBar.getRating() + "",
                            new VolleyResponseListerner() {
                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    ConstantFunctions.toast(TripDetailsActivity.this, response.getString("message"));
                                    startActivity(new Intent(TripDetailsActivity.this, ChooseTypeActivity.class));
                                    finish();
                                }

                                @Override
                                public void onError(String message, String title) {

                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
