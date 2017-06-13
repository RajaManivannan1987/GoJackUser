package com.calljack.im028.calljack.MapIntegrationClasses;

import android.app.Activity;

import com.calljack.im028.calljack.ModelClasses.Pilot;
import com.calljack.im028.calljack.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by IM028 on 8/3/16.
 */
public class MarkerManagement {
    private Activity activity;
    private GoogleMap googleMap;
    Marker marker;


    public MarkerManagement(Activity activity, GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.activity = activity;
    }

    public void addMarkers(List<Pilot> list) {
        googleMap.clear();
        /*if (marker != null) {
            list.clear();
            marker.remove();
        }*/
        for (Pilot pilot : list) {
//            Log.d("WebService", list.toString());
            MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.parseDouble(pilot.getLatitude()), Double.parseDouble(pilot.getLongitude())));
            markerOptions.draggable(false);
            if (pilot.getGender().equalsIgnoreCase("male")) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_icon));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.female_pilot_icon));
            }
            googleMap.addMarker(markerOptions);
        }
    }

    public void clearMarker() {
        if (marker != null) {
            marker.remove();
        }
    }
}
