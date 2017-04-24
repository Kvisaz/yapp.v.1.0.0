package ru.kvisaz.yandextranslate.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.kvisaz.yandextranslate.screens.history.HistoryPresenter;
import ru.kvisaz.yandextranslate.screens.start.StartPresenter;
import ru.kvisaz.yandextranslate.screens.translator.TranslatorPresenter;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class, RepositoryModule.class})
public interface DataComponent {
    void inject(StartPresenter startPresenter);

    void inject(TranslatorPresenter translatorPresenter);

    void inject(HistoryPresenter historyPresenter);
}