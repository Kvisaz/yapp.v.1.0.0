package ru.kvisaz.yandextranslate.screens.history;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.SentencePair;

public interface IHistoryView extends MvpView {
    void showHistory(List<HistoryEntity> entities);


    void addPage(List<SentencePair> sentencePairs);
    void setSearchFieldHint(String hint);
}