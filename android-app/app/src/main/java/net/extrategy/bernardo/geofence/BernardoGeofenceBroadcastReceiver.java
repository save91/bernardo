package net.extrategy.bernardo.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import net.extrategy.bernardo.utilities.InjectorUtils;

public class BernardoGeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BernardoGeofenceBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        BernardoGeofenceService geofenceService =
                InjectorUtils.provideGeofenceService(context);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Error code " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            geofenceService.onEnter();
        } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            geofenceService.onExit();
        } else {
            // Log the error.
            Log.e(TAG, "Invalid type transiction");
        }
    }
}
