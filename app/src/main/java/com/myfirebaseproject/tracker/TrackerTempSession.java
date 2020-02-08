package com.myfirebaseproject.tracker;

import android.content.Intent;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public class TrackerTempSession {
    private static final TrackerTempSession ourInstance = new TrackerTempSession();

    private LatLng updatedLatLng;
    private String updatedLandMark;
    private String floorUnitApartment;
    private int positionOfSelectedItemInList;
    private LatLng carrierCurrentLatLng;
    private float bearing;
    private Intent intent;
    private Place placeForComplteAddress;
    private LatLng lastSelectedLatLng;
    private Map<String, String> makeCompleteAddress;


    public static TrackerTempSession getInstance()
    {
        return ourInstance;
    }

    private TrackerTempSession() {
    }

    public static TrackerTempSession getOurInstance() {
        return ourInstance;
    }

    public LatLng getCarrierCurrentLatLng() {
        return carrierCurrentLatLng;
    }

    public void setCarrierCurrentLatLng(LatLng carrierCurrentLatLng) {
        this.carrierCurrentLatLng = carrierCurrentLatLng;
    }

    public int getPositionOfSelectedItemInList() {
        return positionOfSelectedItemInList;
    }
    public void setPositionOfSelectedItemInList(int positionOfSelectedItemInList) {
        this.positionOfSelectedItemInList = positionOfSelectedItemInList;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Place getPlaceForComplteAddress() {
        return placeForComplteAddress;
    }

    public void setPlaceForComplteAddress(Place placeForComplteAddress) {
        this.placeForComplteAddress = placeForComplteAddress;
    }

    public Map<String, String> getMakeCompleteAddress() {
        return makeCompleteAddress;
    }

    public void setMakeCompleteAddress(Map<String, String> makeCompleteAddress) {
        this.makeCompleteAddress = makeCompleteAddress;
    }

    public LatLng getUpdatedLatLng() {
        return updatedLatLng;
    }

    public String getUpdatedLandMark() {
        return updatedLandMark;
    }

    public String getFloorUnitApartment() {
        return floorUnitApartment;
    }

    public void setUpdatedLatLng(LatLng updatedLatLng, String landMark, String floorUnitApartment) {
        this.updatedLatLng = updatedLatLng;
        this.updatedLandMark = landMark;
        this.floorUnitApartment = floorUnitApartment;
    }

    public LatLng getLastSelectedLatLng() {
        return lastSelectedLatLng;
    }

    public void setLastSelectedLatLng(LatLng lastSelectedLatLng) {
        this.lastSelectedLatLng = lastSelectedLatLng;
    }

}
