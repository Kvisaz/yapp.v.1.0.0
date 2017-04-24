package ru.kvisaz.yandextranslate.screens.history;

import java.util.List;

import ru.kvisaz.yandextranslate.data.models.Translate;

public interface IHistoryPresenter {

    void onPageVisible();

    void onFavoriteCheck(Translate translate, HistoryAdapter.BookmarkCheckedCallback checkedCallback);

    void onHistoryModeSelect(HistoryTabMode mode);

    void onDeleteButtonClick(List<Translate> translatesForRemoving);

    void onSearchFieldChange(String searchText);
}