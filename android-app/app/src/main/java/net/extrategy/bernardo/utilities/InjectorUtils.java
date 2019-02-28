package net.extrategy.bernardo.utilities;

import android.content.Context;

import net.extrategy.bernardo.AppExecutors;
import net.extrategy.bernardo.geofence.BernardoGeofenceService;
import net.extrategy.bernardo.network.BernardoNetworkService;
import net.extrategy.bernardo.ui.MainViewModelFactory;

public class InjectorUtils {

    public static BernardoNetworkService provideNetworService(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return BernardoNetworkService.getInstance(context.getApplicationContext(), executors);
    }

    public static BernardoGeofenceService provideGeofenceService(Context context) {
        return BernardoGeofenceService.getInstance(context.getApplicationContext());
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        BernardoNetworkService networkService = provideNetworService(context.getApplicationContext());
        BernardoGeofenceService geofenceService = provideGeofenceService(context.getApplicationContext());
        return new MainViewModelFactory(networkService, geofenceService);
    }

}