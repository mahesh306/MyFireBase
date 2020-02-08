package com.myfirebaseproject.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TrackBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, TrackerService.class);
        //service.putExtra(IAppConstants.KEY_COMING_FROM, TrackerService.VALUE_CARRIER);
        context.startService(service);
    }
}
