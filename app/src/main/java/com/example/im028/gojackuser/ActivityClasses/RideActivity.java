package com.example.im028.gojackuser.ActivityClasses;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.DialogFragment.CancelTripDialog;
import com.example.im028.gojackuser.DialogFragment.ContactDialog;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.CustomUI.TouchableWrapper;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.ScheduleThread.ScheduleThread;
import com.example.im028.gojackuser.Utility.ScheduleThread.TimerInterface;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideActivity extends MenuCommonActivity {
    private String TAG = "RideActivity";

    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private TouchableWrapper touchableWrapper;
    private TextView toLocationTextView, pickLocationTextView, messageTextView;
    private LinearLayout cancelLinearLayout, toLocationLinearLayout, pickLocationLinearLayout;
    private WebServices webServices;
    private JSONObject jsonObject;
    private LatLng pickLatLng, toLatLng;
    private String pickAddressString = "", toAddressString = "";

    private LinearLayout riderDetailsLinearLayout, rideProcessLinearLayout;
    private CircleImageView riderPhotoCircleImageView;
    private TextView riderNameTextView, riderBikeNameTextView, riderBikeNumberTextView, riderRatingTextView, riderDistanceTextView;
    private LinearLayout callLinearLayout, trackMyRideLinearLayout, cancelRideLinearLayout;
    private boolean waitFlag = true;
    private View.OnClickListener cancel;
    private final Handler handler = new Handler();
    private ScheduleThread scheduleThread;
    private Marker pilotMarker, toMarker, fromMarker;
    private String rideId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_ride);
        webServices = new WebServices(this, TAG);
        rideId = getIntent().getExtras().getString(ConstantValues.rideId);
        scheduleThread = new ScheduleThread(new TimerInterface() {
            @Override
            public void onRun() {
                try {
                    getRiderLocation();
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }, this);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.rideActivityFragment);
        touchableWrapper = (TouchableWrapper) findViewById(R.id.rideActivityTouchableWrapper);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {
                googleMap = googleMap1;
                getData();
                if (ActivityCompat.checkSelfPermission(RideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RideActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });

        toLocationTextView = (TextView) findViewById(R.id.rideActivityToLocationTextView);
        toLocationLinearLayout = (LinearLayout) findViewById(R.id.rideActivityToLocationLinearLayout);
        pickLocationTextView = (TextView) findViewById(R.id.rideActivityPickLocationTextView);
        pickLocationLinearLayout = (LinearLayout) findViewById(R.id.rideActivityPickLocationLinearLayout);

        messageTextView = (TextView) findViewById(R.id.rideActivityMessageTextView);
        cancelLinearLayout = (LinearLayout) findViewById(R.id.rideActivityCancelLinearLayout);

        rideProcessLinearLayout = (LinearLayout) findViewById(R.id.rideActivityRideProcessLinearLayout);
        riderDetailsLinearLayout = (LinearLayout) findViewById(R.id.rideActivityRiderDetailsLinearLayout);
        riderPhotoCircleImageView = (CircleImageView) findViewById(R.id.rideActivityRiderPhotoCircleImageView);
        riderNameTextView = (TextView) findViewById(R.id.rideActivityRiderNameTextView);
        riderBikeNameTextView = (TextView) findViewById(R.id.rideActivityRiderBikeNameTextView);
        riderBikeNumberTextView = (TextView) findViewById(R.id.rideActivityRiderBikeNumberTextView);
        riderRatingTextView = (TextView) findViewById(R.id.rideActivityRiderRatingTextView);
        riderDistanceTextView = (TextView) findViewById(R.id.rideActivityRiderDistanceTextView);

        callLinearLayout = (LinearLayout) findViewById(R.id.rideActivityCallLinearLayout);
        trackMyRideLinearLayout = (LinearLayout) findViewById(R.id.rideActivityTrackLinearLayout);
        cancelRideLinearLayout = (LinearLayout) findViewById(R.id.rideActivityCancelRequestLinearLayout);
        cancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CancelTripDialog contactDialog = new CancelTripDialog();
                    Bundle b = new Bundle();
                    b.putString(ConstantValues.rideId, rideId);
                    b.putString(ConstantValues.rideType, jsonObject.getString("type"));
                    contactDialog.setArguments(b);
                    contactDialog.show(getFragmentManager(), "cancelTripDialog");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
        callLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContactDialog contactDialog = new ContactDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantValues.phoneNumber, jsonObject.getString("mobilenumber"));
                    contactDialog.setArguments(bundle);
                    contactDialog.show(getFragmentManager(), "contactDialog");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        toLocationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(toLatLng));
            }
        });

        pickLocationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(pickLatLng));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleMap != null)
            getData();
        registerReceiver(mMessageReceiver, new IntentFilter(ConstantValues.driverStatus));
    }

    //Must unregister onPause()
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduleThread.stop();
    }

    private void getData() {
        webServices.checkStatusNew(rideId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                try {
                    jsonObject = response.getJSONObject("data");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                switch (jsonObject.getString("type")) {
                    case "courier":
                        setTitle("Courier");
                        break;
                    case "ride":
                        setTitle("Commute");
                        break;
                }
                switch (response.getString("status")) {
                    case "0":
                        scheduleThread.stop();
                        riderDetailsLinearLayout.setVisibility(View.GONE);
                        rideProcessLinearLayout.setVisibility(View.VISIBLE);
                        cancelLinearLayout.setVisibility(View.VISIBLE);
                        messageTextView.setText(response.getString("message"));
                        pickAddressString = jsonObject.getString("startinglocality");
                        toAddressString = jsonObject.getString("endinglocality");
                        pickLatLng = new LatLng(Double.parseDouble(jsonObject.getString("startinglatitude")), Double.parseDouble(jsonObject.getString("startinglongitude")));
                        toLatLng = new LatLng(Double.parseDouble(jsonObject.getString("endinglatitude")), Double.parseDouble(jsonObject.getString("endinglongitude")));
                        if (toMarker == null)
                            toMarker = googleMap.addMarker(new MarkerOptions().draggable(false).position(toLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer_red_half)));
                        else
                            toMarker.setPosition(toLatLng);
                        if (fromMarker == null)
                            fromMarker = googleMap.addMarker(new MarkerOptions().draggable(false).position(pickLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer_green_half)));
                        else
                            fromMarker.setPosition(pickLatLng);
                        waitFlag = false;
                        updateData();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickLatLng, 15));
                        cancelLinearLayout.setOnClickListener(cancel);
                        cancelRideLinearLayout.setOnClickListener(cancel);
                        break;
                    case "1":
                        scheduleThread.start();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        riderDetailsLinearLayout.setVisibility(View.VISIBLE);
                                        rideProcessLinearLayout.setVisibility(View.GONE);

                                    }
                                });
                            }
                        }, 3000);
                        messageTextView.setText(response.getString("message"));
                        updateDriverData();
                        updateData();
                        cancelLinearLayout.setVisibility(View.VISIBLE);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickLatLng, 15));
                        cancelLinearLayout.setOnClickListener(cancel);
                        cancelRideLinearLayout.setOnClickListener(cancel);
                        break;
                    case "2":
                        scheduleThread.stop();
                        switch (jsonObject.getString("type")) {
                            case "courier":
                                startActivity(new Intent(RideActivity.this, ListCourierActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case "ride":
                                startActivity(new Intent(RideActivity.this, DashboardActivity.class));
                                break;
                        }
                        finish();
                        break;
                    case "3":
                        updateDriverData();
                        scheduleThread.stop();
                        sosImg.setVisibility(View.VISIBLE);
                        cancelLinearLayout.setVisibility(View.GONE);
                        riderDetailsLinearLayout.setVisibility(View.VISIBLE);
                        rideProcessLinearLayout.setVisibility(View.GONE);
                        riderDistanceTextView.setText(response.getString("message"));
                        updateData();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickLatLng, 15));
                        cancelLinearLayout.setOnClickListener(cancel);
                        cancelRideLinearLayout.setOnClickListener(cancel);
                        break;
                    case "4":
                        switch (jsonObject.getString("type")) {
                            case "courier":
                                startActivity(new Intent(RideActivity.this, CourierDetailsActivity.class).putExtra(ConstantValues.rideId, rideId));
                                break;
                            case "ride":
                                startActivity(new Intent(RideActivity.this, TripDetailsActivity.class).putExtra("data", jsonObject.toString()));
                                break;
                        }
                        finish();
                        break;
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(RideActivity.this, title, message, false);
            }
        });
    }

    private void updateData() {
        pickLocationTextView.setText(pickAddressString);
        toLocationTextView.setText(toAddressString);
    }

    private void updateDriverData() throws JSONException {
        riderNameTextView.setText(jsonObject.getString("name"));
        riderBikeNameTextView.setText(jsonObject.getString("vehiclemake") + " " + jsonObject.getString("vehiclemodel"));
        riderBikeNumberTextView.setText(jsonObject.getString("vehiclenumber"));
        riderRatingTextView.setText(jsonObject.getString("rating"));
        try {
            Picasso.with(RideActivity.this).load(jsonObject.getString("photo")).into(riderPhotoCircleImageView);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        pickAddressString = jsonObject.getString("startinglocality");
        toAddressString = jsonObject.getString("endinglocality");
        pickLatLng = new LatLng(Double.parseDouble(jsonObject.getString("startinglatitude")), Double.parseDouble(jsonObject.getString("startinglongitude")));
        toLatLng = new LatLng(Double.parseDouble(jsonObject.getString("endinglatitude")), Double.parseDouble(jsonObject.getString("endinglongitude")));
        if (toMarker == null)
            toMarker = googleMap.addMarker(new MarkerOptions().draggable(false).position(toLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer_red_half)));
        else
            toMarker.setPosition(toLatLng);
        if (fromMarker == null)
            fromMarker = googleMap.addMarker(new MarkerOptions().draggable(false).position(pickLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer_green_half)));
        else
            fromMarker.setPosition(pickLatLng);
    }

    private void getRiderLocation() throws JSONException {
        webServices.trackPilotDistance(jsonObject.getString("rideid"), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                riderDistanceTextView.setText("YOUR PILOT WILL ARRIVE IN " + response.getString("time").toUpperCase());
                if (pilotMarker == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_icon));
                    markerOptions.draggable(false);
                    markerOptions.position(new LatLng(Double.parseDouble(response.getString("latitude")), Double.parseDouble(response.getString("longitude"))));
                    pilotMarker = googleMap.addMarker(markerOptions);
                } else {
                    pilotMarker.setPosition(new LatLng(Double.parseDouble(response.getString("latitude")), Double.parseDouble(response.getString("longitude"))));
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

}
