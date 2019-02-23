package net.extrategy.bernardo.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.AppExecutors;

import java.net.URL;

public class BernardoNetworkService {
    private static final String TAG = BernardoNetworkService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardoNetworkService sInstance;
    private final Context mContext;

    private final AppExecutors mExecutors;

    private BernardoNetworkService(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
    }

    public static BernardoNetworkService getInstance(Context context, AppExecutors executors) {
        Log.d(TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new BernardoNetworkService(context.getApplicationContext(), executors);
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
        Log.d(TAG, "fetchPosts");

        mExecutors.networkIO().execute(() -> {
            try {
                URL openDoorUrl = NetworkUtils.buildDoorUrl();
                Log.d(TAG, "openDoorUrl: " + openDoorUrl.toString());


                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(openDoorUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
