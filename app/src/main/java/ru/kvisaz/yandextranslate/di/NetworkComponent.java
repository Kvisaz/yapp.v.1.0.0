package ru.kvisaz.yandextranslate.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.kvisaz.yandextranslate.screens.start.StartPresenter;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(StartPresenter startPresenter);
}