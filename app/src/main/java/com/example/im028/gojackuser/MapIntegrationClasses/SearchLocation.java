package com.example.im028.gojackuser.MapIntegrationClasses;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.Utility.InterfaceClasses.PlaceInterface;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by IM028 on 8/8/16.
 */
public class SearchLocation {
    private String TAG = "SearchLocation";
    private Activity activity;
    private PlaceInterface placeInterface;
    public int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public SearchLocation(Activity activity, PlaceInterface placeInterface) {
        this.activity = activity;
        this.placeInterface = placeInterface;
    }

    public void requestLocation() {

        try {
            LatLngBounds.Builder latLngBounds = LatLngBounds.builder();
            latLngBounds.include(MyApplication.locationInstance().getLocation());
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setBoundsBias(latLngBounds.build())
                    .build(activity);
            activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    public void resultActivity(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                placeInterface.getPlace(PlaceAutocomplete.getPlace(activity, data));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(activity, data);
                placeInterface.getPlace(null);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == activity.RESULT_CANCELED) {
                placeInterface.getPlace(null);
                // The user canceled the operation.
            }
        }
    }
}
