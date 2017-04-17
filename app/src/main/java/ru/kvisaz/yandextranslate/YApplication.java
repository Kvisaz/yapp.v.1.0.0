package ru.kvisaz.yandextranslate;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import ru.kvisaz.yandextranslate.di.ComponentProvider;

public class YApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        ComponentProvider.init(this);
    }
}