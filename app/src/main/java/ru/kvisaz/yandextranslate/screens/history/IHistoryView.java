package ru.kvisaz.yandextranslate.screens.history;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.Translate;

public interface IHistoryView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showHistory(List<Translate> entities);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSearchFieldHint(String hint);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideTranslate(Translate translate);
}