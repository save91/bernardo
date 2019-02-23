package net.extrategy.bernardo.utilities;

import android.content.Context;

import net.extrategy.bernardo.AppExecutors;
import net.extrategy.bernardo.network.BernardoNetworkService;

public class InjectorUtils {

    public static BernardoNetworkService provideNetworService(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return BernardoNetworkService.getInstance(context.getApplicationContext(), executors);
    }

}