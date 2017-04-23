package ru.kvisaz.yandextranslate.screens.history;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.Translate;
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
                        , this::handleServerError);
    }

    @Override
    public void onFavoriteCheck(Translate translate) {
        // just update
        historyDbService.save(translate)
                .subscribe(
                        (id -> {
                            Log.d(Constants.LOG_TAG, "translated for " + translate.getSource() + " favorite = " + translate.isFavorite());
                        })
                        , throwable -> {
                            // todo тут надо отменить выделение кнопки - ведь не сохранилось
                            handleServerError(throwable);
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

    private void handleServerError(Throwable throwable) {
        Log.d(Constants.LOG_TAG, throwable.getMessage());
    }
}
