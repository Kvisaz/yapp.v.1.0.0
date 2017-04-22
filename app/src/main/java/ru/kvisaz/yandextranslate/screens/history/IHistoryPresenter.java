package ru.kvisaz.yandextranslate.screens.history;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;

public interface IHistoryPresenter {

    void onPageVisible();

    void onFavoriteCheck(HistoryEntity entity);

    void onHistorySelect();

    void onFavoritesSelect();

    void onDeleteButtonClick();

    void onSearchFieldChange(String searchText);
}