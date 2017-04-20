package ru.kvisaz.yandextranslate.screens.history;

import ru.kvisaz.yandextranslate.data.models.SentencePair;

public interface IHistoryPresenter {
    void onHistorySelect();
    void onFavoritesSelect();

    void onItemSelect(SentencePair sentencePair);
    void onDeleteButtonClick();

    void onSearchFieldChange(String searchText);
}