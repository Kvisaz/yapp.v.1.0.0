package ru.kvisaz.yandextranslate.screens.history;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.Translate;

public interface IHistoryPresenter {

    void onPageVisible();

    void onFavoriteCheck(Translate translate);

    void onHistorySelect();

    void onFavoritesSelect();

    void onDeleteButtonClick();

    void onSearchFieldChange(String searchText);
}