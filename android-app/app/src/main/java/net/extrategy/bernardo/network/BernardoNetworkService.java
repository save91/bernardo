package net.extrategy.bernardo.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.AppExecutors;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class BernardoNetworkService {
    private static final String TAG = BernardoNetworkService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardoNetworkService sInstance;
    private static BernardoAPI sBernardoAPI;
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
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                sBernardoAPI = retrofit.create(BernardoAPI.class);
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

    private class BernardoResponse {
        String message;
    }

    private interface BernardoAPI {
        @GET("api/v1.0/door")
        Call<BernardoResponse> door();

        @GET("api/v1.0/gate")
        Call<BernardoResponse> gate();
    }

    public void openDoor() {
        Log.d(TAG, "open door");

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = sBernardoAPI.door();
            try {
                BernardoResponse result = callDoor.execute().body();
                Log.d(TAG, result.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void openGate() {
        Log.d(TAG, "open gate");

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = sBernardoAPI.gate();
            try {
                BernardoResponse result = callDoor.execute().body();
                Log.d(TAG, result.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
