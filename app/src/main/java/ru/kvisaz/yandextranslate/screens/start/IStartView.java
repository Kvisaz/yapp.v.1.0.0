package ru.kvisaz.yandextranslate.screens.start;

import com.arellomobile.mvp.MvpView;

interface IStartView extends MvpView {
    void showErrorScreen(String title, String message);
    void showOfflineScreen();
    void goToTabActivityScreen();
}
