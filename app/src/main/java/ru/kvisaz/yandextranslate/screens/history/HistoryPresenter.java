package ru.kvisaz.yandextranslate.screens.history;

import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.utils.StringUtils;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.models.Translate;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class HistoryPresenter extends MvpPresenter<IHistoryView> implements IHistoryPresenter {

    @Inject
    HistoryDbService historyDbService;

    @Inject
    Handler handler;

    private Runnable fetchSearchRunnable;
    private HistoryTabMode historyMode = HistoryTabMode.HISTORY;
    private String searchString = "";

    public HistoryPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onPageVisible() {
        fetchSearch();
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
        historyMode = mode;
        fetchSearch();
    }

    @Override
    public void onDeleteButtonClick(List<Translate> translatesForRemoving) {
        if (translatesForRemoving.size() == 0) return;
        historyDbService.delete(translatesForRemoving)
                .subscribe(
                        number -> getViewState().hideTranslate(translatesForRemoving),
                        this::handleServerError);
    }

    @Override
    public void onSearchFieldChange(String searchText) {
        String cleanedInput = StringUtils.cleanInput(searchText);

        // если редактирование продолжается, отменяем заказ на поиск, отправляем только когда набор закончился
        handler.removeCallbacks(fetchSearchRunnable);

        searchString = cleanedInput;

        // заказываем запрос на поиск, который исполнится, если набор закончен
        // (в течение некоторого времени не будет меняться текст)
        fetchSearchRunnable = this::fetchSearch;
        handler.postDelayed(fetchSearchRunnable, Constants.DELAY_SEARCH_DB_CHANGING_MS);
    }

    private void fetchSearch() {
        Observable<List<Translate>> listObservable;
        boolean emptySearch = searchString.length() == 0;
        boolean isFavoriteMode = historyMode == HistoryTabMode.FAVORITES;
        listObservable = emptySearch ? historyDbService.fetchHistoryList(isFavoriteMode) : historyDbService.fetchSearchLike(searchString, isFavoriteMode);

        listObservable.subscribe(
                translates -> getViewState().showHistory(translates),
                this::handleServerError
        );
    }

    private void handleServerError(Throwable throwable) {
        Log.d(Constants.LOG_TAG, throwable.getMessage());
    }
}
