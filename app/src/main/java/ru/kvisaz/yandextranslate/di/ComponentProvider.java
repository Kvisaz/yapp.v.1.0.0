package ru.kvisaz.yandextranslate.di;

import android.content.Context;

public class ComponentProvider {
    private static NetworkComponent mNetworkComponent;

    public static void init(Context context) {
        mNetworkComponent = DaggerNetworkComponent
                .builder()
                .appModule(new AppModule(context))
                .networkModule(new NetworkModule())
                .build();
    }

    public static NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }
}