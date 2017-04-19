package ru.kvisaz.yandextranslate.screens.start;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.ConnectivityChecker;
import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.rest.YandexService;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class StartPresenter extends MvpPresenter<IStartView> implements IStartPresenter {

    @Inject
    YandexService mYandexService;

    @Inject
    ConnectivityChecker mConnectivityChecker;

    @Inject
    LocaleChecker localeChecker;

    @Inject
    UserSettings userSettings;

    public StartPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onActivityCreate() {
        if (isConnected()) {
            fetchLanguagesData();
        } else {
            getViewState().showOfflineScreen();
        }
    }

    @Override
    public void onStartButtonClick() {
        getViewState().goToTabActivityScreen();
    }


    private boolean isConnected() {
        return mConnectivityChecker.isOnline();
    }

    /*
    *   1. Запрашиваем у сервера список поддерживаемых языков с кодом ui из пользовательских настроек
    *   2. Если в настройках ничего нет, берем код ui из локали
    *   3. Если текущая локаль уникальна или не подошла - повторяем запрос с дефолтным ui
    * */
    private void fetchLanguagesData() {

        // 1 и 2
        String currentUserLanguageCode = userSettings.getLanguageCode();
        if (currentUserLanguageCode == null) {
            currentUserLanguageCode = localeChecker.getLanguageCode();
            userSettings.setLanguageCode(currentUserLanguageCode);
        }

        // задаем минимальное время для показа индикатора
        Observable<Long> timer = Observable.timer(Constants.START_SCREEN_LOADING_MIN_TIME, TimeUnit.SECONDS);
        Observable<LanguagesResponse> fetchLanguages = mYandexService
                .fetchLanguages(currentUserLanguageCode) // первый запрос к серверу с кодом ui
                .flatMap(this::checkLanguageResponse); //  если ui не подошел, делаем второй с дефолтным кодом

        // группируем таймер и запрос к серверу - результат будет только когда отработают оба observable,
        // то есть будет минимальное время показа стартового экрана
        Observable.zip(fetchLanguages, timer, (languagesResponse, timerValue) -> languagesResponse)
                .subscribe(
                        languageData -> {
                            ActiveSession.saveLanguageData(languageData);
                            getViewState().goToTabActivityScreen();
                        },

                        throwable -> {
                            getViewState().showErrorScreen("Error", throwable.getMessage());
                        });
    }

    /*
    *    Если параметр ui не подошел, то ставим дефолтный язык и повторяем запрос
    *    для получения
    * */
    private Observable<LanguagesResponse> checkLanguageResponse(LanguagesResponse languagesResponse) {
        if (languagesResponse.langs == null) {
            userSettings.setLanguageCode(Constants.DEFAULT_LANGUAGE_CODE);
            return mYandexService.fetchLanguages(userSettings.getLanguageCode());
        } else {
            return Observable.just(languagesResponse);
        }
    }

}