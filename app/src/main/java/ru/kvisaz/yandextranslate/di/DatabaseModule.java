package ru.kvisaz.yandextranslate.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kvisaz.yandextranslate.data.UserSettings;

@Module
public class DatabaseModule {
    @Provides
    @NonNull
    @Singleton
    public UserSettings provideUserSettings() {
        final UserSettings userSettings = new UserSettings();
        return userSettings;
    }
}