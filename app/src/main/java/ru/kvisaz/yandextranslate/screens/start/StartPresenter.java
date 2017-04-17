package ru.kvisaz.yandextranslate.screens.start;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.ConnectivityChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.rest.YandexService;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class StartPresenter extends MvpPresenter<IStartView> implements IStartPresenter {

    @Inject
    YandexService mYandexService;

    @Inject
    ConnectivityChecker mConnectivityChecker;

    public StartPresenter(){
        super();
        ComponentProvider.getNetworkComponent().inject(this);
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

    private void fetchLanguagesData() {

        // use timer observable for minimal start screen show time
        Observable<Long> timer = Observable.timer(Constants.START_SCREEN_LOADING_MIN_TIME, TimeUnit.SECONDS);
        Observable<LanguagesResponse> fetchLanguages = mYandexService.fetchLanguages();

        Observable.zip(fetchLanguages, timer, (user, timerValue) -> user)
                .subscribe(
                        languageData -> {
                            ActiveSession.saveLanguageData(languageData);
                            getViewState().goToTabActivityScreen();
                        },

                        throwable -> {
                            getViewState().showErrorScreen("Error", throwable.getMessage());
                        });
    }

}