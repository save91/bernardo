package net.extrategy.bernardo.utilities;

import android.content.Context;

import net.extrategy.bernardo.network.BernardoNetworkService;

public class InjectorUtils {

    public static BernardoNetworkService provideNetworService(Context context) {
        return BernardoNetworkService.getInstance(context.getApplicationContext());
    }

}