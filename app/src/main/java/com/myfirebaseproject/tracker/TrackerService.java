package com.myfirebaseproject.tracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myfirebaseproject.R;
import com.myfirebaseproject.model.User;
import com.myfirebaseproject.utils.PreferenceUtils;

import java.io.File;
import static com.myfirebaseproject.utils.TrackingUtilities.getCurrentTimeStamp;


public class TrackerService extends Service {

    private final IBinder mBinder = new MyBinder();

    public static final String STORE_LOCATIONS  = "storelocations";
    private static final String TAG = TrackerService.class.getSimpleName();
    private static final String CHANNEL_ID = "2";
    private Location mLastStoredLocation;
    private PreferenceUtils preferenceUtils;
    public String mComingFrom;
    private int mCount;
    private float mBearing;
    private int mLastCarriersShipmentID;


    @Override
    public IBinder onBind(Intent intent) {
       // mComingFrom = intent.getStringExtra(IAppConstants.KEY_COMING_FROM);
        return mBinder;}

    @Override
    public void onCreate() {
        super.onCreate();
        preferenceUtils = new PreferenceUtils(this);

        buildNotification();
        requestLocationUpdates();

    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.location_track_info))
                .setOngoing(true)
                .setContentIntent(broadcastIntent);
                //.setSmallIcon(R.drawable.ic_tracker);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            channel.setSound(null, null);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(3000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (preferenceUtils.getServiceStatus().equals("true")) {
                        onNewLocationArrived(locationResult);
                    }
                }
            }, null);
        }else{
            stopSelf();
        }
    }

    private void onNewLocationArrived(LocationResult locationResult) {

        Location prevLocation = new Location("");
        LatLng previousLocationLatLng =  TrackerTempSession.getInstance().getCarrierCurrentLatLng();
        if (previousLocationLatLng != null) {
            prevLocation.setLatitude(previousLocationLatLng.latitude);
            prevLocation.setLongitude(previousLocationLatLng.longitude);
        }
        LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                locationResult.getLastLocation().getLongitude());

        TrackerTempSession.getInstance().setCarrierCurrentLatLng(latLng);
        Location currentLocation =  new Location("");
        currentLocation.setLatitude(latLng.latitude);
        currentLocation.setLongitude(latLng.longitude);
        if(prevLocation.getLongitude() != 0 &&  currentLocation.getLongitude() != 0)        // marker direction
        TrackerTempSession.getInstance().setBearing(prevLocation.bearingTo(currentLocation));
        mBearing = prevLocation.bearingTo(currentLocation);


        final String livePath = getString(R.string.firebase_path) + File.separator ;

        final String storePath = getString(R.string.firebase_path) + File.separator + STORE_LOCATIONS
                + File.separator + preferenceUtils.getName();



        DatabaseReference refLive = FirebaseDatabase.getInstance().getReference(livePath);
        DatabaseReference refStore = FirebaseDatabase.getInstance().getReference(storePath);


        Location location = locationResult.getLastLocation();
        if (location != null) {
            Log.d(TAG, "location update" + location);
            User liveUser = new User(location.getLatitude(), location.getLongitude(),
                    mBearing,  getMetersMoved(currentLocation), getCurrentTimeStamp());
            User storeUser = new User(location.getLatitude(), location.getLongitude(),
                    mBearing, getMetersMoved(currentLocation), getCurrentTimeStamp());
            if(getMetersMoved(currentLocation) >= 99 ) {           // securing old location not to override with small chnage(. < X). Preventing if user going slowly that data will not be saved.
               mLastStoredLocation = currentLocation;
                // NO NEED THIS COMMENT.
            /*    if(mLastCarriersShipmentID != preferenceUtils.getLastCarrierShipmentID())  // wherever shipment is changed it has to be set to zero.
                    mCount = 0;*/
               refStore.child(String.valueOf(mCount++)).setValue(storeUser);
              // mLastCarriersShipmentID = preferenceUtils.getId();
            }



            refLive.child(String.valueOf(preferenceUtils.getName())).setValue(liveUser);
        }
    }


    private int getMetersMoved(Location currentLocation) {
        if(mLastStoredLocation != null)
        return Math.round(mLastStoredLocation.distanceTo(currentLocation));
        else {
            mLastStoredLocation = currentLocation;
            return 0;
        }
    }

    public class MyBinder extends Binder {
       public TrackerService getService() {
            return TrackerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}