package ru.kvisaz.yandextranslate.di;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kvisaz.yandextranslate.common.LocaleChecker;

@Module
@Singleton
public class AppModule {
    private Context mContext;

    public AppModule(@NonNull Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    LocaleChecker providesLocaleChecker(Context context) {
        final LocaleChecker localeChecker = new LocaleChecker(context);
        return localeChecker;
    }
}
