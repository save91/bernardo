package net.extrategy.bernardo.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.utilities.InjectorUtils;

public class BernardoIntentService extends IntentService {
    private static final String TAG = BernardoIntentService.class.getSimpleName();

    public BernardoIntentService() {
        super("BernardoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Intent service started");
        BernardoNetworkService networkService =
                InjectorUtils.provideNetworService(this.getApplicationContext());
        networkService.openDoor();
    }
}
