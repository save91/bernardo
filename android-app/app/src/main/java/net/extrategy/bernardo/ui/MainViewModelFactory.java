package net.extrategy.bernardo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import net.extrategy.bernardo.network.BernardoNetworkService;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final BernardoNetworkService mNetworkService;

    public MainViewModelFactory(BernardoNetworkService networkService) {
        mNetworkService = networkService;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mNetworkService);
    }
}
