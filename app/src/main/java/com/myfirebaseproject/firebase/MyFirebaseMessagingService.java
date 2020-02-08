package com.myfirebaseproject.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myfirebaseproject.R;
import com.myfirebaseproject.activity.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data: " + remoteMessage.getData().toString());

            try {
                String strTitle = remoteMessage.getData().get("title");
                String strBody = remoteMessage.getData().get("body");

                sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            }catch (Exception ee){
                System.out.println(ee.getMessage());
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message data: " + remoteMessage.getData().toString());
            try {
                sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            }catch (Exception ee){
                System.out.println(ee.getMessage());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendNotification(String title, String body) {
        try {

            Intent intent = new Intent(this, MainActivity.class);

            //if(intent != null) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);             //new line added 06/01/19

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

//            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT  );

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationId = 123;
            String channelId = "Tracking";
            String channelName = "topline technologies";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon( R.mipmap.ic_launcher_round)
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(notificationId)
                    .setSound(defaultSoundUri)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body));

            if(contentIntent != null && intent != null) {
                mBuilder.setContentIntent(contentIntent);
            }
            notificationManager.notify(100+notificationId, mBuilder.build());

        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            Log.e("TAG", ee.getMessage());
        }
    }
}