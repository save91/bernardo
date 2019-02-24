package net.extrategy.bernardo.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import net.extrategy.bernardo.network.BernardoNetworkService;

public class MainActivityViewModel extends ViewModel {
    private BernardoNetworkService mBernardoNetwordService;

    public MainActivityViewModel(BernardoNetworkService networkService) {
        mBernardoNetwordService = networkService;
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

}
