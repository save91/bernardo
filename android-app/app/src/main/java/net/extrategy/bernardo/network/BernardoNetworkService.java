package net.extrategy.bernardo.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.AppExecutors;
import net.extrategy.bernardo.R;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class BernardoNetworkService {
    private static final String TAG = BernardoNetworkService.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static BernardoNetworkService sInstance;
    private static BernardoAPI sBernardoAPI;
    private final Context mContext;

    private static MutableLiveData<Boolean> mIsOpeningTheDoor;
    private static MutableLiveData<Boolean> mIsOpeningTheGate;
    private static MutableLiveData<String> mSuccess;
    private static MutableLiveData<String> mError;

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
                mIsOpeningTheDoor = new MutableLiveData<>();
                mIsOpeningTheGate = new MutableLiveData<>();
                mSuccess = new MutableLiveData<>();
                mError = new MutableLiveData<>();

                mIsOpeningTheDoor.postValue(false);
                mIsOpeningTheGate.postValue(false);
                mSuccess.postValue(null);
                mError.postValue(null);

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
        @FormUrlEncoded
        @POST("action")
        Call<BernardoResponse> action(@Field("id") String first, @Field("cs") String last);
    }

    public void openDoor() {
        Log.d(TAG, "open door");
        mIsOpeningTheDoor.postValue(true);

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = sBernardoAPI.action("1", "test");
            try {
                BernardoResponse result = callDoor.execute().body();
                Log.d(TAG, result.message);
                mSuccess.postValue(mContext.getResources().getString(R.string.success_door));
            } catch (Exception e) {
                mError.postValue(mContext.getResources().getString(R.string.error));
                e.printStackTrace();
            }
            mIsOpeningTheDoor.postValue(false);
        });
    }

    public void openGate() {
        Log.d(TAG, "open gate");
        mIsOpeningTheGate.postValue(true);

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = sBernardoAPI.action("2", "test");
            try {
                BernardoResponse result = callDoor.execute().body();
                Log.d(TAG, result.message);
                mSuccess.postValue(mContext.getResources().getString(R.string.success_gate));
            } catch (Exception e) {
                mError.postValue(mContext.getResources().getString(R.string.error));
                e.printStackTrace();
            }
            mIsOpeningTheGate.postValue(false);
        });
    }

    public LiveData<Boolean> isOpeningTheDoor() {
        return mIsOpeningTheDoor;
    }

    public LiveData<Boolean> isOpeningTheGate() {
        return mIsOpeningTheGate;
    }

    public LiveData<String> onSuccess() {
        return mSuccess;
    }

    public LiveData<String> onError() {
        return mError;
    }

}
