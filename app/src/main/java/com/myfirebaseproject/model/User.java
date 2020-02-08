package com.myfirebaseproject.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public double LATITUDE;
    public double LONGITUDE;
    public double BEARING;
    public int METERS_MOVED;
    public long LOCATION_TIME_STAMP;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(double LATITUDE, double LONGITUDE, double BEARING) {
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.BEARING = BEARING;
    }

    public User(double LATITUDE, double LONGITUDE, double BEARING, int METERS_MOVED, long LOCATION_TIME_STAMP) {
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.BEARING = BEARING;
        this.METERS_MOVED = METERS_MOVED;
        this.LOCATION_TIME_STAMP = LOCATION_TIME_STAMP;
    }
}