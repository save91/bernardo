package net.extrategy.bernardo.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BernardoNetworkService {
    private static final String TAG = BernardoNetworkService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardoNetworkService sInstance;
    private final Context mContext;

    private BernardoNetworkService(Context context) {
        mContext = context;
    }

    public static BernardoNetworkService getInstance(Context context) {
        Log.d(TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new BernardoNetworkService(context.getApplicationContext());
                Log.d(TAG, "Made new network data source");
            }
        }

        return sInstance;
    }

    public void startOpenDoorService() {
        Intent intentToFetch = new Intent(mContext, BernardoIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(TAG, "Service created");
    }


    public void openDoor() {
        Log.d(TAG, "open door");
    }

}
