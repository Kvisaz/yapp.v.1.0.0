package ru.kvisaz.yandextranslate.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kvisaz.yandextranslate.data.TranslateRepository;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.rest.DictRestService;
import ru.kvisaz.yandextranslate.data.rest.TranslateRestService;

@Module
public class RepositoryModule {

    @Provides
    @NonNull
    @Singleton
    public TranslateRepository providesTranslateRepository(TranslateRestService translateRestService,
                                                           DictRestService dictRestService,
                                                           HistoryDbService historyDbService) {
        return new TranslateRepository(translateRestService, dictRestService, historyDbService);
    }
}