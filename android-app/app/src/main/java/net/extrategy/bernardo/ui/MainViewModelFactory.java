package net.extrategy.bernardo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import net.extrategy.bernardo.geofence.BernardoGeofenceService;
import net.extrategy.bernardo.network.BernardoNetworkService;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final BernardoNetworkService mNetworkService;
    private final BernardoGeofenceService mGeofenceService;

    public MainViewModelFactory(BernardoNetworkService networkService, BernardoGeofenceService geofenceService) {
        mNetworkService = networkService;
        mGeofenceService = geofenceService;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mNetworkService, mGeofenceService);
    }
}
