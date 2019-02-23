package net.extrategy.bernardo.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import net.extrategy.bernardo.utilities.InjectorUtils;

public class BernardoIntentService extends IntentService {
    private static final String TAG = BernardoIntentService.class.getSimpleName();
    public static final String EXTRA_ACTION = "ACTION";
    public static final String ACTION_DOOR = "DOOR";
    public static final String ACTION_GATE = "GATE";

    public BernardoIntentService() {
        super("BernardoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Intent service started");
        BernardoNetworkService networkService =
                InjectorUtils.provideNetworService(this.getApplicationContext());
        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null) {
            return;
        }

        if (action.equals(ACTION_DOOR)) {
            networkService.openDoor();
        }

        if (action.equals(ACTION_GATE)) {
            networkService.openGate();
        }
    }
}
