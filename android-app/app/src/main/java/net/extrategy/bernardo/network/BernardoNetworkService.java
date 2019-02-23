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
        Intent intent = new Intent(mContext, BernardoIntentService.class);
        intent.putExtra(BernardoIntentService.EXTRA_ACTION, BernardoIntentService.ACTION_DOOR);
        mContext.startService(intent);
        Log.d(TAG, "Service created");
    }

    public void startOpenGateService() {
        Intent intent = new Intent(mContext, BernardoIntentService.class);
        intent.putExtra(BernardoIntentService.EXTRA_ACTION, BernardoIntentService.ACTION_GATE);
        mContext.startService(intent);
        Log.d(TAG, "Service created");
    }


    public void openDoor() {
        Log.d(TAG, "open door");

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

    public void openGate() {
        Log.d(TAG, "open gate");

        mExecutors.networkIO().execute(() -> {
            try {
                URL openGateUrl = NetworkUtils.buildGateUrl();
                Log.d(TAG, "openGateUrl: " + openGateUrl.toString());


                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(openGateUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
