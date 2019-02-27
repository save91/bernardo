package net.extrategy.bernardo.geofence;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import net.extrategy.bernardo.utilities.NotificationUtils;

public class BernardoGeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = BernardoGeofenceTransitionsIntentService.class.getSimpleName();

    public BernardoGeofenceTransitionsIntentService() {
        super("BernardoGeofenceTransitionsIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Error code " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            NotificationUtils.remindUserBecauseIsCloser(getApplicationContext());
        } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            NotificationUtils.clearAllNotifications(getApplicationContext());
        } else {
            // Log the error.
            Log.e(TAG, "Invalid type transiction");
        }
    }
}
