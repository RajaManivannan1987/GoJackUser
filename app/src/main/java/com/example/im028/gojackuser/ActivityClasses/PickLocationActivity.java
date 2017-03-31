package com.example.im028.gojackuser.ActivityClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.CustomUI.TouchableWrapper;
import com.example.im028.gojackuser.Utility.InterfaceClasses.TouchInterface;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;
import java.util.Locale;

public class PickLocationActivity extends BackCommonActivity {
    private SupportMapFragment supportMapFragment;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private GoogleMap googleMap;
    private LatLng location;
    private TouchableWrapper touchableWrapper;
    private Marker marker;
    private final int MY_LOCATION = 1;
    private Button saveTextView;
    private boolean isTouch = false;
    private String address;
    private Address returnedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pick_location);
        saveTextView = (Button) findViewById(R.id.okButton);
        touchableWrapper = (TouchableWrapper) findViewById(R.id.locationPickerActivityTouchableWrapper);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationPickerActivityMapFragment);
        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.locationPickerActivityPlaceAutoCompleteFragment);
        LatLngBounds.Builder latLngBounds = LatLngBounds.builder();
        latLngBounds.include(MyApplication.locationInstance().getLocation());
        placeAutocompleteFragment.setBoundsBias(latLngBounds.build());
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                PickLocationActivity.this.googleMap = googleMap;
                if (MyApplication.locationInstance().getLocation() != null) {
                    location = MyApplication.locationInstance().getLocation();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(googleMap.getCameraPosition().target);
                markerOptions.draggable(false);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer_red_half));
                marker = googleMap.addMarker(markerOptions);
                enableMyLocation();

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        marker.setPosition(PickLocationActivity.this.googleMap.getCameraPosition().target);
                        if (!isTouch) {
                            placeAutocompleteFragment.setText(getMarkerMovedAddress(PickLocationActivity.this.googleMap.getCameraPosition().target));
                        }
                    }
                });
            }
        });
        touchableWrapper.setTouchInterface(new TouchInterface() {
            @Override
            public void onPressed() {
                isTouch = true;
            }

            @Override
            public void onReleased() {
                isTouch = false;
                placeAutocompleteFragment.setText(getMarkerMovedAddress(googleMap.getCameraPosition().target));
            }

            @Override
            public void onMove() {
//                marker.setPosition(googleMap.getCameraPosition().target);
            }
        });
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            }

            @Override
            public void onError(Status status) {

            }
        });
        saveTextView.setVisibility(View.VISIBLE);
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "";
                if (returnedAddress == null) return;
                if (returnedAddress.getSubLocality() != null) {
                    location = returnedAddress.getSubLocality();
                } else if (returnedAddress.getLocality() != null) {
                    location = returnedAddress.getLocality();
                } else {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(ConstantValues.locationPickerTitle, location);
                intent.putExtra(ConstantValues.locationPickerAddress, address);
                intent.putExtra(ConstantValues.locationPickerLatitude, googleMap.getCameraPosition().target.latitude);
                intent.putExtra(ConstantValues.locationPickerLongitude, googleMap.getCameraPosition().target.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private String getMarkerMovedAddress(LatLng dragPosition) {
        String adres = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                returnedAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (i != 0)
                        stringBuilder.append(",");
                    stringBuilder.append(returnedAddress.getAddressLine(i));
                }
                adres = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.address = adres;
        return adres;
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(PickLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PickLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PickLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
