package com.example.im028.gojackuser.ActivityClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.DialogFragment.CouponDialog;
import com.example.im028.gojackuser.MapIntegrationClasses.MarkerManagement;
import com.example.im028.gojackuser.MapIntegrationClasses.SearchLocation;
import com.example.im028.gojackuser.ModelClasses.Pilot;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.CustomUI.TouchableWrapper;
import com.example.im028.gojackuser.Utility.InterfaceClasses.PlaceInterface;
import com.example.im028.gojackuser.Utility.InterfaceClasses.TouchInterface;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends MenuCommonActivity {
    private final static String TAG = "DashboardActivity";
    private WebServices webServices;
    private Gson gson = new Gson();
    private static final int PICKLOCATION = 1, TOLOCATION = 2;
    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private TouchableWrapper touchableWrapper;
    private MarkerManagement markerManagement;
    private List<Pilot> pilotList = new ArrayList<Pilot>();
    private ImageView markerImageView;
    private TextView pickUpLocationTextView, toLocationTextView;
    private LinearLayout pickLinearLayout, toLinearLayout;
    private LatLng pickLatLng, toLatLng;
    private String pickAddressString = "", toAddressString = "", dateTimeValue = "", fare = "", scheduleType = "";
    private int locationType = PICKLOCATION;
    private SearchLocation searchLocation;
    private Button scheduleButton, ScheduleButton1;
    private boolean flagTouchPressed = false, isMoved = false;

    private LinearLayout disableLinearLayout, enableLinearLayout, couponLinearLayout, sheduleLinearLayout;
    public TextView couponTextView;
    public static String couponId = "0";
    private TextView fareTextView;
    private FloatingActionButton locationFloatingActionButton, location1FloatingActionButton;
    private View.OnClickListener currentLocation;
    private TextView paymentModeTextView, scheduleDateTimeTextView;
    private Button disabledRequestRideButton, enabledRequestRideButton;
    private final int paymentTypeRequestCode = 1;
    private final int scheduleRequestCode = 2;
    private final int couponRequestCode = 3;
   /* private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            flagTouchPressed = false;
            getPilotLocation(googleMap.getCameraPosition().target);
            updateAddress();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_dashboard);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        webServices = new WebServices(this, TAG);
        couponTextView = (TextView) findViewById(R.id.dashboardActivityCouponCodeTextView);
       /* if (getIntent().getExtras() != null) {
            couponTextView.setText(getIntent().getExtras().getString("couponName"));
            couponId = getIntent().getExtras().getString("couponId");
            updateAddress();
        }*/

        searchLocation = new SearchLocation(this, new PlaceInterface() {
            @Override
            public void getPlace(Place place) {
                if (place != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                } else if (locationType == TOLOCATION) {
                    if (toLatLng == null) {
                        pickLocation();
                    }
                }
            }
        });
        //Map Integration Starts
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dashboardActivityFragment);
        touchableWrapper = (TouchableWrapper) findViewById(R.id.dashboardActivityTouchableWrapper);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                getMapReady(googleMap);
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        if (!flagTouchPressed) {
                            getPilotLocation(DashboardActivity.this.googleMap.getCameraPosition().target);
                            updateAddress();
                        }
                    }
                });
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                });
            }
        });

        touchableWrapper.setTouchInterface(new TouchInterface() {
            @Override
            public void onPressed() {
                flagTouchPressed = true;
                isMoved = false;
            }

            @Override
            public void onReleased() {
                flagTouchPressed = false;
                getPilotLocation(googleMap.getCameraPosition().target);
                updateAddress();
            }

            @Override
            public void onMove() {
                isMoved = true;

            }
        });
        //Map Integration Ends

        //Address Integration Starts
        markerImageView = (ImageView) findViewById(R.id.dashboardActivityMapMarkerImageView);
        pickUpLocationTextView = (TextView) findViewById(R.id.dashboardActivityPickLocationTextView);
        pickUpLocationTextView.setSelected(true);
        pickLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivityPickLocationLinearLayout);
        toLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivityToLocationLinearLayout);
        toLocationTextView = (TextView) findViewById(R.id.dashboardActivityToLocationTextView);
        toLocationTextView.setSelected(true);
        scheduleButton = (Button) findViewById(R.id.dashboardActivityScheduleButton);
        sheduleLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivitysheduleLinearLayout);
        ScheduleButton1 = (Button) findViewById(R.id.dashboardActivityScheduleButton1);
        pickLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickLocation();
            }
        });
        toLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLocation();
            }
        });
        //Address Integration Ends

        //Bottom Integration Starts
        enableLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivityEnabledLinearLayout);
        disableLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivityDisabledLinearLayout);
        fareTextView = (TextView) findViewById(R.id.dashboardActivityFareTextView);
        /*enableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        locationFloatingActionButton = (FloatingActionButton) findViewById(R.id.dashboardActivityCurrentLocationFloatingActionButton);
        location1FloatingActionButton = (FloatingActionButton) findViewById(R.id.dashboardActivityCurrentLocation1FloatingActionButton);
        currentLocation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(MyApplication.locationInstance().getLocation()));
            }
        };
        locationFloatingActionButton.setOnClickListener(currentLocation);
        location1FloatingActionButton.setOnClickListener(currentLocation);
        disabledRequestRideButton = (Button) findViewById(R.id.dashboardActivityDisableRequestRideButton);
        enabledRequestRideButton = (Button) findViewById(R.id.dashboardActivityEnabledRequestRideButton);
        paymentModeTextView = (TextView) findViewById(R.id.dashboardActivityPaymentModeTextView);
        scheduleDateTimeTextView = (TextView) findViewById(R.id.dashboardActivityscheduleTextView);
        couponLinearLayout = (LinearLayout) findViewById(R.id.dashboardActivityCouponLinearLayout);
        couponLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, CouponActivity.class);
                i.putExtra(ConstantValues.couponType, "ride");
                startActivityForResult(i, couponRequestCode);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
               /* CouponDialog couponDialog = new CouponDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantValues.couponType, "ride");
                couponDialog.setArguments(bundle);
                couponDialog.show(getFragmentManager(), "CouponDialog");*/
            }
        });
        disabledRequestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pilotList.size() == 0) {
                    ConstantFunctions.toast(DashboardActivity.this, "No pilots available");
                } else {
                    ConstantFunctions.toast(DashboardActivity.this, "Select To Address");
                }
            }
        });
        enabledRequestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float distance = new ConstantFunctions().getDistance(pickLatLng, toLatLng);
                if (distance >= 100 && pickLatLng != null && toLatLng != null) {
                    webServices.requestRide(pickLatLng, toLatLng, pickAddressString, toAddressString, paymentModeTextView.getText().toString(), couponId, dateTimeValue, fare, new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (!response.getString("status").equalsIgnoreCase("0")) {
                                couponId = "0";
                                ConstantFunctions.toast(DashboardActivity.this, response.getString("message"));
                                Log.d(TAG, scheduleType);
                                if (scheduleType.equalsIgnoreCase("schedule")) {
                                    startActivity(new Intent(DashboardActivity.this, ScheduleTripListActivity.class));
                                    finish();
                                } else {
                                    startActivity(RideActivity.getRideId(DashboardActivity.this, response.getString("rideid")));
                                    // today 7/4/2017

//                                    startActivity(new Intent(DashboardActivity.this, RideActivity.class).putExtra(ConstantValues.rideId, response.getString("rideid")));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(DashboardActivity.this, title, message, false);
                        }
                    });
                } else {
                    ConstantFunctions.toast(DashboardActivity.this, "Pickup and Delivery locations can't be same and not null.");
                }
            }
        });
        paymentModeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PaymentMethodActivity.class);
                intent.putExtra(ConstantValues.paymentType, paymentModeTextView.getText().toString());
                startActivityForResult(intent, paymentTypeRequestCode);
            }
        });
        sheduleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                scheduledIntent();
            }
        });
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduledTrip();
            }
        });
        ScheduleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduledTrip();
            }
        });

        //Bottom Integration Ends
//        handler.postDelayed(runnable, 100);

    }

    private void scheduledTrip() {
        if (pickLatLng != null && toLatLng != null) {
            scheduledIntent();
            if (enableLinearLayout.getVisibility() == View.GONE) {
                enableLinearLayout.setVisibility(View.VISIBLE);
                disableLinearLayout.setVisibility(View.GONE);
                getFare();
            } else {
//                enableLinearLayout.setVisibility(View.GONE);
//                disableLinearLayout.setVisibility(View.VISIBLE);
            }
        } else
            ConstantFunctions.toast(DashboardActivity.this, "Select To Address");
    }

    private void scheduledIntent() {
        Intent intent = new Intent(DashboardActivity.this, ScheduleActivity.class);
        startActivityForResult(intent, scheduleRequestCode);
    }

    private void pickLocation() {
        if (locationType == PICKLOCATION) {
            locationType = PICKLOCATION;
            searchLocation.requestLocation();
        } else {
            locationType = PICKLOCATION;
            getPilotLocation(googleMap.getCameraPosition().target);
        }
        markerImageView.setImageResource(R.drawable.map_pointer_green);
        pickLinearLayout.setBackgroundResource(R.drawable.address_background);
        toLinearLayout.setBackgroundResource(R.drawable.address_background_unselected);

        if (pickLatLng != null) {
            changeCameraPosition(pickLatLng);
        }
        visibleLocationButton(true);
    }

    private void toLocation() {
        if (locationType == TOLOCATION || toLatLng == null) {
            searchLocation.requestLocation();
        }
        markerImageView.setImageResource(R.drawable.map_pointer_red);
        toLinearLayout.setBackgroundResource(R.drawable.address_background);
        pickLinearLayout.setBackgroundResource(R.drawable.address_background_unselected);
        locationType = TOLOCATION;
        googleMap.clear();
        if (toLatLng != null) {
            changeCameraPosition(toLatLng);
        }
        visibleLocationButton(false);
    }

    private void visibleLocationButton(boolean b) {
        if (b) {
            location1FloatingActionButton.setVisibility(View.VISIBLE);
            locationFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            location1FloatingActionButton.setVisibility(View.GONE);
            locationFloatingActionButton.setVisibility(View.GONE);
        }
    }

    private void getMapReady(GoogleMap googleMap) {
        DashboardActivity.this.googleMap = googleMap;
        changeCameraPosition(MyApplication.locationInstance().getLocation());
        enableCurrentLocation();
        markerManagement = new MarkerManagement(DashboardActivity.this, googleMap);
    }

    private void changeCameraPosition(LatLng latLng) {
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                updateAddress();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void getPilotLocation(LatLng latLng) {
        if (locationType == PICKLOCATION)
            webServices.getPilotLocation(latLng.latitude + "", latLng.longitude + "", new VolleyResponseListerner() {
                @Override
                public void onResponse(JSONObject response) throws JSONException {
                    googleMap.clear();
                    pilotList.clear();
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        if (!response.getString("message").startsWith("No Driver Found")) {
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                pilotList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), Pilot.class));
                            }
                        } else {
                            ConstantFunctions.toast(DashboardActivity.this, "No pilots available");
                            googleMap.clear();
                            markerManagement.clearMarker();
                            if (enableLinearLayout.getVisibility() == View.VISIBLE) {
                                enableLinearLayout.setVisibility(View.GONE);
                            }
                        }
//                        if (response.getJSONArray("data").length() != 0) {
//                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
//                                pilotList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), Pilot.class));
//                            }
//                        } else {
//                            ConstantFunctions.toast(DashboardActivity.this,"No pilot available in pickup location");
//                            googleMap.clear();
//                            markerManagement.clearMarker();
//                        }
                    }
                    markerManagement.addMarkers(pilotList);
                }

                @Override
                public void onError(String message, String title) {
                    ConstantFunctions.showSnakBar(message, toLocationTextView);
//                    AlertDialogManager.showAlertDialog(DashboardActivity.this, title, message, false);
                }
            });
    }

    private void updateAddress() {
        getAddress();
        if (pickLatLng != null && toLatLng != null && !pilotList.isEmpty()) {
            enableLinearLayout.setVisibility(View.VISIBLE);
            disableLinearLayout.setVisibility(View.GONE);
            getFare();
        } else {
            if (toLatLng == null)
                enableLinearLayout.setVisibility(View.GONE);
            disableLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getFare() {
        webServices.getFareEstimation(pickLatLng, toLatLng, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    fareTextView.setText(response.getString("data"));
                    fare = response.getString("data");
                }
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, toLocationTextView);
            }
        });
    }

    private void getAddress() {
        // 1- pick and 2 - to
        if (locationType == PICKLOCATION) {
            pickLatLng = googleMap.getCameraPosition().target;
            pickAddressString = ConstantFunctions.getMarkerMovedAddress(DashboardActivity.this, googleMap.getCameraPosition().target);
            pickUpLocationTextView.setText(pickAddressString);
        } else {
            toAddressString = ConstantFunctions.getMarkerMovedAddress(DashboardActivity.this, googleMap.getCameraPosition().target);
            toLatLng = googleMap.getCameraPosition().target;
            toLocationTextView.setText(toAddressString);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        flagTouchPressed = false;
        switch (requestCode) {
            case couponRequestCode:
                if (resultCode == RESULT_OK) {
                    couponId = data.getExtras().getString("couponId");
                    couponTextView.setText(data.getExtras().getString("couponName"));
                }
                break;
            case paymentTypeRequestCode:
                if (resultCode == RESULT_OK) {
                    paymentModeTextView.setText(data.getExtras().getString(ConstantValues.paymentType, "cash"));
                }
                break;
            case scheduleRequestCode:
                if (resultCode == RESULT_OK) {
                    scheduleDateTimeTextView.setText(data.getExtras().getString(ConstantValues.scheduleDateTime, "Now"));
                    dateTimeValue = data.getExtras().getString(ConstantValues.scheduleDateTime1, "");
                    scheduleType = "schedule";
                }
                break;
            default:
        }
        searchLocation.resultActivity(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //handler.removeCallbacks(runnable);
    }
}
