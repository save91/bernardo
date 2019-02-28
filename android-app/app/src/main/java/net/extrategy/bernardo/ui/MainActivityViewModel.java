package net.extrategy.bernardo.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import net.extrategy.bernardo.geofence.BernardoGeofenceService;
import net.extrategy.bernardo.network.BernardoNetworkService;

public class MainActivityViewModel extends ViewModel {
    private BernardoNetworkService mBernardoNetwordService;
    private BernardoGeofenceService mBernardoGeofenceService;

    public MainActivityViewModel(BernardoNetworkService networkService, BernardoGeofenceService geofenceService) {
        mBernardoNetwordService = networkService;
        mBernardoGeofenceService = geofenceService;
    }

    public LiveData<Boolean> isOpeningTheDoor() {
        return mBernardoNetwordService.isOpeningTheDoor();
    }

    public LiveData<Boolean> isOpeningTheGate() {
        return mBernardoNetwordService.isOpeningTheGate();
    }

    public LiveData<String> onError() {
        return mBernardoNetwordService.onError();
    }

    public LiveData<String> onSuccess() {
        return mBernardoNetwordService.onSuccess();
    }

    public LiveData<Boolean> isCloserToExtrategy() {
        return mBernardoGeofenceService.isCloserToExtrategy();
    }
}
