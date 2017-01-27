package com.example.im028.gojackuser.DialogFragment;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im028.gojackuser.R;


public class PilotHereDialogActivity extends AppCompatActivity {
    private final String TAG = "PilotHereDialogActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_pilot_here);
        findViewById(R.id.cancel_pilot_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void claimOffer() {

    }
}
