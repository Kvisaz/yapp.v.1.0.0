package ru.kvisaz.yandextranslate.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.database.HistoryDatabaseHelper;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;

@Module
public class DatabaseModule {
    @Provides
    @NonNull
    @Singleton
    public UserSettings provideUserSettings() {
        final UserSettings userSettings = new UserSettings();
        return userSettings;
    }

    @Provides
    @Singleton
    public HistoryDatabaseHelper providesHistoryDbHelper(Context context) {
        return new HistoryDatabaseHelper(context);
    }

    @Provides
    @Singleton
    public SQLiteDatabase providesSqlLiteDatabas(HistoryDatabaseHelper historyDatabaseHelper) {
        return historyDatabaseHelper.getWritableDatabase();
    }

    @Provides
    @Singleton
    public HistoryDbService providesHistoryService(SQLiteDatabase sqLiteDatabase) {
        return new HistoryDbService(sqLiteDatabase);
    }
}