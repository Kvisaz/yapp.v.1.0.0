package ru.kvisaz.yandextranslate.screens.history;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.database.HistoryService;
import ru.kvisaz.yandextranslate.data.models.SentencePair;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class HistoryPresenter extends MvpPresenter<IHistoryView> implements IHistoryPresenter {

    @Inject
    HistoryService historyService;

    public HistoryPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onPageVisible() {
        historyService.fetchHistory()
                .subscribe(
                        (entities -> {
                            getViewState().showHistory(entities);
                        })
                        , this::handleThrowable);
    }

    @Override
    public void onHistorySelect() {

    }

    @Override
    public void onFavoritesSelect() {

    }

    @Override
    public void onItemSelect(SentencePair sentencePair) {

    }

    @Override
    public void onDeleteButtonClick() {

    }

    @Override
    public void onSearchFieldChange(String searchText) {

    }

    private void handleThrowable(Throwable throwable) {
        Log.d(Constants.LOG_TAG, throwable.getMessage());
    }
}
