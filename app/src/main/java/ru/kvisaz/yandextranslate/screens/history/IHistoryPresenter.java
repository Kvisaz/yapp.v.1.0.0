package ru.kvisaz.yandextranslate.screens.history;

public interface IHistoryPresenter {

    void onPageVisible();

    void onHistorySelect();

    void onFavoritesSelect();

    void onDeleteButtonClick();

    void onSearchFieldChange(String searchText);
}