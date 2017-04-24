package ru.kvisaz.yandextranslate.data;

import android.util.Log;

import io.reactivex.Observable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.RxService;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Translate;
import ru.kvisaz.yandextranslate.data.rest.DictRestService;
import ru.kvisaz.yandextranslate.data.rest.TranslateRestService;
import ru.kvisaz.yandextranslate.data.rest.models.DictResponse;
import ru.kvisaz.yandextranslate.data.rest.models.TranslateResponse;

public class TranslateRepository extends RxService {

    private TranslateRestService translateRestService;
    private DictRestService dictRestService;
    private HistoryDbService historyDbService;

    public TranslateRepository(TranslateRestService translateRestService, DictRestService dictRestService, HistoryDbService historyDbService) {
        this.translateRestService = translateRestService;
        this.dictRestService = dictRestService;
        this.historyDbService = historyDbService;
    }


    /*
    *   Запрос на перевод
    *   1. сначала проверяет аналогичный запрос в истории,
    *      и только если такого нет - обращается к серверам
    *   2. как побочный эффект при получении - записываем в базу данных новый запрос
    * */
    public Observable<Translate> fetchTranslate(String source, String from, String to, String ui) {
        Observable<HistoryEntity> historyEntityObservable = historyDbService.fetchSearchSame(source, from, to);

        Observable<Translate> translateObservableAfterHistoryCheck = historyEntityObservable
                .flatMap(historyEntity -> { // берем перевод из базы данных или с сервера
                    if (historyEntity._id != null) {
                        // todo delete
                        Log.d(Constants.LOG_TAG, "read historyEntity id = " + historyEntity._id);

                        return Observable.defer(() -> Observable.just(new Translate(historyEntity)));
                    } else {
                        // todo delete
                        Log.d(Constants.LOG_TAG, "getTranslateDictObservable");
                        return getTranslateDictObservable(source, from, to, ui);
                    }
                });
        // todo how to save???

        return translateObservableAfterHistoryCheck.compose(applySchedulers());
    }


    /*
    *   Создает комплексный запрос к API Translate и Dict
    * */
    private Observable<Translate> getTranslateDictObservable(String source,
                                                             String from,
                                                             String to,
                                                             String ui) {

        Observable<DictResponse> dictResponseObservable = dictRestService
                .fetchDefinition(source, from, to, ui)
                .onErrorReturn(throwable -> new DictResponse()); // Словарный запрос - обслуживающий, поэтому в случае ошибки предоставляем пользователю пустую словарную статью, чтобы выполнить основную функцию - перевод

        Observable<TranslateResponse> translateResponseObservable = translateRestService
                .fetchTranslate(source, from, to);

        return Observable.zip(translateResponseObservable, dictResponseObservable, (translateResponse, dictResponse) -> {
            Translate translate = new Translate();
            translate.setSource(source);
            translate.setText(translateResponse.text.get(0));
            translate.setFrom(from);
            translate.setTo(to);
            translate.setDictArticle(new DictArticle(dictResponse));
            saveOnlyNewTranslate(translate);
            return translate;
        });
    }

    private void saveOnlyNewTranslate(Translate translate) {
        if (translate.isNew) {
            // отправляем на запись в базу, если перевод новый
            historyDbService.save(translate).subscribe(
                    id -> {
                        // todo delete
                        Log.d(Constants.LOG_TAG, "historyDbService.save success, id- " + id);
                    },
                    throwable -> {
                        // todo delete
                        Log.d(Constants.LOG_TAG, "historyDbService.save error - " + throwable.getMessage());

                    }
            );
        }
    }
}
