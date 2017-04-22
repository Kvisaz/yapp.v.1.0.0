package ru.kvisaz.yandextranslate.screens.history;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;

public interface IHistoryView extends MvpView {
    void showHistory(List<HistoryEntity> entities);

    void setSearchFieldHint(String hint);
}