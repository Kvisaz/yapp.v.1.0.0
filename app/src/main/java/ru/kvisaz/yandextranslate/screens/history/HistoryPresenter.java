package ru.kvisaz.yandextranslate.screens.history;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class HistoryPresenter extends MvpPresenter<IHistoryView> implements IHistoryPresenter {

    @Inject
    HistoryDbService historyDbService;

    public HistoryPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onPageVisible() {
        historyDbService.fetchHistory()
                .subscribe(
                        (entities -> {
                            getViewState().showHistory(entities);
                        })
                        , this::handleThrowable);
    }

    @Override
    public void onFavoriteCheck(HistoryEntity entity) {
        // just update
        historyDbService.save(entity)
                .subscribe(
                        (id -> {
                            Log.d(Constants.LOG_TAG, "translated for " + entity.source + " favorite = " + entity.isFavorite);
                        })
                        , throwable -> {
                            // todo тут надо отменить выделение кнопки - ведь не сохранилось
                            handleThrowable(throwable);
                        }
                );
    }

    @Override
    public void onHistorySelect() {

    }

    @Override
    public void onFavoritesSelect() {

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
