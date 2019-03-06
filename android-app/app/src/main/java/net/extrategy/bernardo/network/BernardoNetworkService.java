package net.extrategy.bernardo.network;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.AppExecutors;
import net.extrategy.bernardo.BernardoWidgetProvider;
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


    private final BernardoAPI mBernardoAPI;
    private final Context mContext;
    private final AppWidgetManager mAppWidgetManager;

    private final MutableLiveData<Boolean> mIsOpeningTheDoor;
    private final MutableLiveData<Boolean> mIsOpeningTheGate;
    private final MutableLiveData<String> mSuccess;
    private final MutableLiveData<String> mError;

    private final AppExecutors mExecutors;

    private BernardoNetworkService(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mBernardoAPI = retrofit.create(BernardoAPI.class);

        mAppWidgetManager = AppWidgetManager.getInstance(mContext);

        mIsOpeningTheDoor = new MutableLiveData<>();
        mIsOpeningTheGate = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
        mError = new MutableLiveData<>();

        mIsOpeningTheDoor.postValue(false);
        mIsOpeningTheGate.postValue(false);
        mSuccess.postValue(null);
        mError.postValue(null);
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

    private class BernardoResponse {
        String success;
    }

    private interface BernardoAPI {
        @FormUrlEncoded
        @POST("ws_monostable.htm")
        Call<BernardoResponse> action(@Field("id") String first, @Field("cs") String last);
    }

    public void openDoor() {
        Log.d(TAG, "open door");
        mIsOpeningTheDoor.postValue(true);

        int[] appWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(mContext, BernardoWidgetProvider.class));
        BernardoWidgetProvider.updateLoading(mContext, mAppWidgetManager, true, appWidgetIds);

        String id = mContext.getString(R.string.id_door);
        String secret = mContext.getString(R.string.secret);

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = mBernardoAPI.action(id, secret);
            try {
                BernardoResponse result = callDoor.execute().body();
                mSuccess.postValue(mContext.getResources().getString(R.string.success_door));
                Log.i(TAG, result.success);
                mSuccess.postValue(null);
            } catch (Exception e) {
                mError.postValue(mContext.getResources().getString(R.string.error));
                e.printStackTrace();
                mError.postValue(null);
            }
            mIsOpeningTheDoor.postValue(false);
            BernardoWidgetProvider.updateLoading(mContext, mAppWidgetManager, false, appWidgetIds);
        });
    }

    public void openGate() {
        Log.d(TAG, "open gate");
        mIsOpeningTheGate.postValue(true);
        String id = mContext.getString(R.string.id_gate);
        String secret = mContext.getString(R.string.secret);

        mExecutors.networkIO().execute(() -> {
            Call<BernardoResponse> callDoor = mBernardoAPI.action(id, secret);
            try {
                BernardoResponse result = callDoor.execute().body();
                mSuccess.postValue(mContext.getResources().getString(R.string.success_gate));
                Log.d(TAG, result.success);
                mSuccess.postValue(null);
            } catch (Exception e) {
                mError.postValue(mContext.getResources().getString(R.string.error));
                e.printStackTrace();
                mError.postValue(null);
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
