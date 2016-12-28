package com.example.im028.gojackuser.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.ChooseTypeActivity;
import com.example.im028.gojackuser.ActivityClasses.LocationCheckActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 12/1/2016.
 */

public class GenderRequestDialogActivity extends AppCompatActivity {
    private String TAG = GenderRequestDialogActivity.class.getName();
    private Button genderRequestYesButton, genderRequestNoButton;
    private WebServices webServices;
    private String gender, requestid, message;
    private TextView genderRequestTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_request);
        gender = getIntent().getExtras().getString(ConstantValues.genderType);
        requestid = getIntent().getExtras().getString(ConstantValues.requestid);
        message = getIntent().getExtras().getString(ConstantValues.message);
        webServices = new WebServices(GenderRequestDialogActivity.this, TAG);
        genderRequestTextView = (TextView) findViewById(R.id.genderRequestTextView);
        genderRequestYesButton = (Button) findViewById(R.id.genderRequestYesButton);
        genderRequestNoButton = (Button) findViewById(R.id.genderRequestNoButton);
        genderRequestTextView.setText(message);
        genderRequestYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderRequestStatus("1", gender, requestid);
            }
        });
        genderRequestNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderRequestStatus("0", gender, requestid);
                finish();
            }
        });
    }

    private void genderRequestStatus(String status, String gender, String requestid) {
        webServices.genderRequest(status, gender, requestid, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    ConstantFunctions.toast(GenderRequestDialogActivity.this, response.getString("message"));
                    startActivity(LocationCheckActivity.getLocationCheck(GenderRequestDialogActivity.this, ConstantValues.rideTypeRide));
                    finish();
                } else if (response.getString("status").equalsIgnoreCase("0")) {
                    Log.d("volleyPostData", "volleyPostData");
                    startActivity(LocationCheckActivity.getLocationCheck(GenderRequestDialogActivity.this, ConstantValues.rideTypeRide));
                    finish();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
