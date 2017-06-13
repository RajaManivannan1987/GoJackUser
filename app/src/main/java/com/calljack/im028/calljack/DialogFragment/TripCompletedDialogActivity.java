package com.calljack.im028.calljack.DialogFragment;


import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Singleton.ActionCompletedSingleton;


public class TripCompletedDialogActivity extends AppCompatActivity {
    private final String TAG = "PilotHereDialogActivity";
    Button cancel;
    NotificationManager nMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_trip_end);
        nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        cancel = (Button) findViewById(R.id.cancel_trip_end_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripCompletedDialogActivity.this.finish();
                nMgr.cancelAll();
                ActionCompletedSingleton.getNopilot().ActionCompleted();
            }
        });

    }
}
