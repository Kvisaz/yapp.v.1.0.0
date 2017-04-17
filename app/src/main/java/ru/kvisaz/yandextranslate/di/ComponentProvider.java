package ru.kvisaz.yandextranslate.di;

import android.content.Context;

public class ComponentProvider {

    private static DataComponent mDataComponent;

    public static void init(Context context) {
        mDataComponent = DaggerDataComponent.builder()
                .appModule(new AppModule(context))
                .networkModule(new NetworkModule())
                .databaseModule(new DatabaseModule())
                .build();
    }

    public static DataComponent getDataComponent() {
        return mDataComponent;
    }
}