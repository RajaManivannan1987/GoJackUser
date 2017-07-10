package com.calljack.im028.calljack.ActivityClasses;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.DialogFragment.CancelTripDialog;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Singleton.AddCouponSingleton;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.CustomUI.TouchableWrapper;
import com.calljack.im028.calljack.Utility.InterfaceClasses.CompletedInterface;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.ScheduleThread.ScheduleThread;
import com.calljack.im028.calljack.Utility.ScheduleThread.TimerInterface;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideActivity extends MenuCommonActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String TAG = "RideActivity";
    private static String rideId;

    public static Intent getRideId(Activity activity, String rideId1) {
        rideId = rideId1;
        return new Intent(activity, RideActivity.class).putExtra(ConstantValues.rideId, rideId1);
        //   ride or courier
    }

    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CallGetData", "BroadcastReceiver");
            getData(rideId);
        }
    };

    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private TouchableWrapper touchableWrapper;
    private TextView toLocationTextView, pickLocationTextView, messageTextView;
    private LinearLayout cancelLinearLayout, toLocationLinearLayout, pickLocationLinearLayout;
    private WebServices webServices;
    private Session session;
    private JSONObject jsonObject;
    private LatLng pickLatLng, toLatLng;
    private String pickAddressString = "", toAddressString = "", driverName = "", driverPhoneNo = "", vehicleNo = "", userName = "", orderId = "";

    private LinearLayout riderDetailsLinearLayout, rideProcessLinearLayout;
    private CircleImageView riderPhotoCircleImageView;
    private TextView riderNameTextView, riderBikeNameTextView, riderBikeNumberTextView, riderRatingTextView, riderDistanceTextView;
    private LinearLayout callLinearLayout, trackMyRideLinearLayout, cancelRideLinearLayout;
    private boolean waitFlag = true;
    private View.OnClickListener cancel;
    private final Handler handler = new Handler();
    private final Handler handler1 = new Handler();
    private ScheduleThread scheduleThread;
    private Marker pilotMarker, toMarker, fromMarker;
    private boolean isOnTrip = false;
    MarkerOptions onTripMarkerOption = new MarkerOptions();

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_ride);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        webServices = new WebServices(this, TAG);
        session = new Session(RideActivity.this, TAG);
        userName = new Session(RideActivity.this, TAG).getName();
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
                Log.d("CallGetData", "getMapAsync");
                getData(rideId);
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
                buildGoogleApiClient();
            }
        });

        toLocationTextView = (TextView) findViewById(R.id.rideActivityToLocationTextView);
        toLocationTextView.setSelected(true);
        toLocationLinearLayout = (LinearLayout) findViewById(R.id.rideActivityToLocationLinearLayout);
        pickLocationTextView = (TextView) findViewById(R.id.rideActivityPickLocationTextView);
        pickLocationTextView.setSelected(true);
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
                    ConstantFunctions.call(RideActivity.this, jsonObject.getString("mobilenumber"));
                  /*  ContactDialog contactDialog = new ContactDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantValues.phoneNumber, jsonObject.getString("mobilenumber"));
                    contactDialog.setArguments(bundle);
                    contactDialog.show(getFragmentManager(), "contactDialog");*/
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        trackMyRideLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra("sms_body", userName + " has shared CallJack ride info" + "\n" + "Driver info: " + driverName + "/" + driverPhoneNo + "/" + vehicleNo + "\n" + "Track: " + ConstantValues.TRACKRIDEURL + rideId);
                    intent.setData(Uri.parse("sms:"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
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
        AddCouponSingleton.getChagePilotStatus().setListener(new CompletedInterface() {
            @Override
            public void completed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        riderDistanceTextView.setText("Your pilot is here");
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mMessageReceiver, new IntentFilter(ConstantValues.driverStatus));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleMap != null)
            getData(rideId);
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

    private void getData(final String rideId) {
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
                        isOnTrip = false;
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
                        isOnTrip = false;
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
                        isOnTrip = false;
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
                        isOnTrip = true;
                        scheduleThread.stop();
                        sosImg.setVisibility(View.VISIBLE);
                        cancelLinearLayout.setVisibility(View.GONE);
                        riderDetailsLinearLayout.setVisibility(View.VISIBLE);
                        rideProcessLinearLayout.setVisibility(View.GONE);
                        if (!riderNameTextView.getText().toString().equalsIgnoreCase("")) {
                            riderNameTextView.setText("");
                        }
                        updateDriverData();
                        updateData();
                        riderDistanceTextView.setText(response.getString("message"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickLatLng, 15));
                        cancelLinearLayout.setOnClickListener(cancel);
                        cancelRideLinearLayout.setOnClickListener(cancel);
                        break;
                    case "4":
                        isOnTrip = false;
                        if (jsonObject.getString("mode").equalsIgnoreCase("paytm")) {
                            withDrawAmount(jsonObject.getString("cash"));
                        }
                        switch (jsonObject.getString("type")) {
                            case "courier":
                                startActivity(new Intent(RideActivity.this, CourierDetailsActivity.class).putExtra("rideId", rideId));
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
//                AlertDialogManager.showAlertDialog(RideActivity.this, title, message, false);
                ConstantFunctions.showSnakBar(message, toLocationTextView);
            }
        });
    }

    private void withDrawAmount(String amount) {
        Random r = new Random(System.currentTimeMillis());
        orderId = "dialjackOrder_Id" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(100);

        webServices.generateWithDrawChecksum(orderId, session.getCustomerId(), "1", ConstantValues.WITHDRAW_RQUESTTYPE, session.getPaytmtoken(), "9865132365", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                ConstantFunctions.toast(RideActivity.this, response.getString("ResponseMessage"));
            }

            @Override
            public void onError(String message, String title) {

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
            Picasso.with(RideActivity.this).load(jsonObject.getString("photo"))
                    .placeholder(R.drawable.rider_user_icon)
                    .error(R.drawable.rider_user_icon)
                    .resize(250, 200).into(riderPhotoCircleImageView);
//            Picasso.with(RideActivity.this).load(jsonObject.getString("photo")).into(riderPhotoCircleImageView);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        pickAddressString = jsonObject.getString("startinglocality");
        toAddressString = jsonObject.getString("endinglocality");
        driverName = jsonObject.getString("name");
        driverPhoneNo = jsonObject.getString("mobilenumber");
        vehicleNo = jsonObject.getString("vehiclenumber");

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
//                riderDistanceTextView.setText("YOUR PILOT IS HERE");
                if (pilotMarker == null && !isOnTrip) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_icon));
                    markerOptions.draggable(false);
                    markerOptions.position(new LatLng(Double.parseDouble(response.getString("latitude")), Double.parseDouble(response.getString("longitude"))));
                    pilotMarker = googleMap.addMarker(markerOptions);
                } else {
                    try {
                        pilotMarker.setPosition(new LatLng(Double.parseDouble(response.getString("latitude")), Double.parseDouble(response.getString("longitude"))));
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, riderNameTextView);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        if (location != null && isOnTrip) {
            setMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void setMarker(LatLng currentLocation) {
        if (pilotMarker != null) {
            pilotMarker.remove();
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLocation, 17);
        googleMap.animateCamera(update);
        onTripMarkerOption = new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_ride_icon)).title("").anchor(0.5f, 1f);
        pilotMarker = googleMap.addMarker(onTripMarkerOption);
        animateMarker(pilotMarker, currentLocation, false);
    }

    private void animateMarker(final Marker pilotMarker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(pilotMarker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                pilotMarker.setPosition(new LatLng(lat, lng));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 17);
                }
                {
                    if (hideMarker) {
                        pilotMarker.setVisible(false);
                    } else {
                        pilotMarker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            buildGoogleApiClient();
        }
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                onConnected(Bundle.EMPTY);
            }
        }, 5000);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
