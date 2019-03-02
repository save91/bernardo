package net.extrategy.bernardo.geofence;

import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

import net.extrategy.bernardo.R;
import net.extrategy.bernardo.utilities.NotificationUtils;

public class BernardoGeofenceService {
    private static final String TAG = BernardoGeofenceService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardoGeofenceService sInstance;

    private static final Integer PENDING_INTENT_ID = 124;

    private final MutableLiveData<Boolean> mIsCloserToExtrategy;
    private final Context mContext;
    private final GeofencingClient mGeofencingClient;

    private BernardoGeofenceService(Context context, GeofencingClient geofencingClient) {
        mContext = context;
        mGeofencingClient = geofencingClient;
        mIsCloserToExtrategy = new MutableLiveData<>();
        mIsCloserToExtrategy.postValue(false);
    }

    public static BernardoGeofenceService getInstance(Context context) {
        Log.d(TAG, "Getting the geofencing service");
        if (sInstance == null) {
            synchronized (LOCK) {
                GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
                sInstance = new BernardoGeofenceService(context.getApplicationContext(), geofencingClient);

                Log.d(TAG, "Made new geofencing service");
            }
        }

        return sInstance;
    }

    public void registerGeofencing() {
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
            Log.e(TAG, "securityException: " + securityException.getMessage());
        }
    }

    public void unregisterGeofencing() {
        try {
            mGeofencingClient.removeGeofences(getGeofencePendingIntent())
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
            Log.e(TAG, "securityException: " + securityException.getMessage());
        }
    }

    public void onEnter() {
        mIsCloserToExtrategy.postValue(true);
        NotificationUtils.remindUserBecauseIsCloser(mContext);
    }

    public void onExit() {
        mIsCloserToExtrategy.postValue(false);
        NotificationUtils.clearAllNotifications(mContext);
    }

    private Geofence createGeofencing() {
        String key = mContext.getString(R.string.geofence_key);
        Float radiusInMeter = Float.valueOf(mContext.getString(R.string.geofence_meters));
        Double extrategyLat = Double.valueOf(mContext.getString(R.string.geofence_latitude));
        Double extrategyLong = Double.valueOf(mContext.getString(R.string.geofence_longitude));

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
        Intent intent = new Intent(mContext, BernardoGeofenceBroadcastReceiver.class);
        PendingIntent geofencePendingIntent = PendingIntent.getBroadcast(mContext, PENDING_INTENT_ID, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    public LiveData<Boolean> isCloserToExtrategy() {
        return mIsCloserToExtrategy;
    }
}
