package net.extrategy.bernardo.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class BernardGeofenceService {
    private static final String TAG = BernardGeofenceService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardGeofenceService sInstance;

    private static final Integer PENDING_INTENT_ID = 124;

    private final Context mContext;
    private final GeofencingClient mGeofencingClient;

    private BernardGeofenceService(Context context, GeofencingClient geofencingClient) {
        mContext = context;
        mGeofencingClient = geofencingClient;
    }

    public static BernardGeofenceService getInstance(Context context) {
        Log.d(TAG, "Getting the geofencing service");
        if (sInstance == null) {
            synchronized (LOCK) {
                GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
                sInstance = new BernardGeofenceService(context.getApplicationContext(), geofencingClient);

                Log.d(TAG, "Made new geofencing service");
            }
        }

        return sInstance;
    }

    public void initGeofencing() {
        try {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException securityException) {
            Log.e(TAG, "securityException:" + securityException.getMessage());
        }
    }

    private Geofence createGeofencing() {
        String key = "extrategy";
        Float radiusInMeter = Float.valueOf("50");
        Double extrategyLat = 43.556865;
        Double extrategyLong = 13.2797;

        Geofence geofence = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(key)

                .setCircularRegion(
                        extrategyLat,
                        extrategyLong,
                        radiusInMeter
                )
                .setExpirationDuration(1000 * 60 * 60 * 24)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        return  geofence;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(createGeofencing());
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(mContext, BernardoGeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        PendingIntent geofencePendingIntent = PendingIntent.getService(mContext, PENDING_INTENT_ID, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}
