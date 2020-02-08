package com.myfirebaseproject.activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myfirebaseproject.R;
import com.myfirebaseproject.utils.DoubleUtilities;

import java.util.HashMap;

public class LocationTrack extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tracking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.supportMap);
        mapFragment.getMapAsync(this);
        subscribeToUpdates();
    }


    private void subscribeToUpdates() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        try {
             String key = dataSnapshot.getKey();
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
            double lat = Double.parseDouble(value.get("LATITUDE").toString());
            double lng = Double.parseDouble(value.get("LONGITUDE").toString());
            String bearing = String.valueOf(value.get("BEARING"));
            LatLng location = new LatLng(lat, lng);
            if (!mMarkers.containsKey(key)) {
                mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.user))));
            } else {
                mMarkers.get(key).setPosition(location);
                if (DoubleUtilities.isValidDouble(bearing))
                    mMarkers.get(key).setRotation(Float.parseFloat(bearing));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
        }catch (Exception ee){
            System.out.println(ee.getMessage());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOUNDS_INDIA.getCenter(),4));
    }

}
