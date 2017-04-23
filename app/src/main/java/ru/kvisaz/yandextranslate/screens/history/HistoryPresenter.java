package ru.kvisaz.yandextranslate.screens.history;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.models.Translate;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class HistoryPresenter extends MvpPresenter<IHistoryView> implements IHistoryPresenter {

    @Inject
    HistoryDbService historyDbService;

    private HistoryTabMode historyMode = HistoryTabMode.HISTORY;

    public HistoryPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onPageVisible() {
        historyDbService.fetchHistoryList(false)
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
                            if (historyMode == HistoryTabMode.FAVORITES && !translate.isFavorite()) {
                                getViewState().hideTranslate(translate);
                            }
                            Log.d(Constants.LOG_TAG, "translated for " + translate.getSource() + " favorite = " + translate.isFavorite());
                        })
                        , throwable -> {
                            // todo тут надо отменить выделение кнопки - ведь не сохранилось
                            handleServerError(throwable);
                        }
                );
    }

    @Override
    public void onHistoryModeSelect(HistoryTabMode mode) {
        Observable<List<Translate>> historyObservable;
        historyMode = mode;
        if (mode == HistoryTabMode.HISTORY) {
            historyObservable = historyDbService.fetchHistoryList();
        } else {
            historyObservable = historyDbService.fetchFavorites();
        }

        historyObservable.subscribe(
                (entities -> {
                    getViewState().showHistory(entities);
                })
                , this::handleServerError);


    }

    @Override
    public void onDeleteButtonClick(List<Translate> translatesForRemoving) {
        if (translatesForRemoving.size() == 0) return;
        historyDbService.delete(translatesForRemoving)
                .subscribe(
                        (number -> {
                            getViewState().hideTranslate(translatesForRemoving);
                        })
                        , this::handleServerError);
    }

    @Override
    public void onSearchFieldChange(String searchText) {

    }

    private void handleServerError(Throwable throwable) {
        Log.d(Constants.LOG_TAG, throwable.getMessage());
    }
}
