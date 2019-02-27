package net.extrategy.bernardo.utilities;

import android.content.Context;

import net.extrategy.bernardo.AppExecutors;
import net.extrategy.bernardo.geofence.BernardGeofenceService;
import net.extrategy.bernardo.network.BernardoNetworkService;
import net.extrategy.bernardo.ui.MainViewModelFactory;

public class InjectorUtils {

    public static BernardoNetworkService provideNetworService(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return BernardoNetworkService.getInstance(context.getApplicationContext(), executors);
    }

    public static BernardGeofenceService provideGeofenceService(Context context) {
        return BernardGeofenceService.getInstance(context.getApplicationContext());
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        BernardoNetworkService networkService = provideNetworService(context.getApplicationContext());
        return new MainViewModelFactory(networkService);
    }

}