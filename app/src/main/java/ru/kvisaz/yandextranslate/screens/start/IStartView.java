package ru.kvisaz.yandextranslate.screens.start;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

interface IStartView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showErrorScreen(String title, String message);

    @StateStrategyType(SingleStateStrategy.class)
    void showOfflineScreen();

    void goToTabActivityScreen();
}
