package ru.kvisaz.yandextranslate.data;

import io.reactivex.Observable;
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
        Observable<HistoryEntity> historyEntityObservable = historyDbService.fetchSearchSame(source);

        Observable<Translate> translateObservableAfterHistoryCheck = historyEntityObservable
                .flatMap(historyEntity -> { // берем перевод из базы данных или с сервера
                    if (historyEntity._id != null) {
                        return Observable.defer(() -> Observable.just(new Translate(historyEntity)));
                    } else {
                        return getTranslateDictObservable(source, from, to, ui);
                    }
                });
        // how to save???

        return translateObservableAfterHistoryCheck.compose(applySchedulers());
    }


    /*
    *   Создает комплексный запрос к API Translate и Dict
    * */
    private Observable<Translate> getTranslateDictObservable(String source,
                                                             String from,
                                                             String to,
                                                             String ui) {

        Observable<TranslateResponse> translateResponseObservable = translateRestService.fetchTranslate(source, from, to);
        Observable<DictResponse> dictResponseObservable = dictRestService.fetchDefinition(source, from, to, ui);

        return Observable.zip(translateResponseObservable, dictResponseObservable, (translateResponse, dictResponse) -> {
            Translate translate = new Translate();
            translate.setSource(source);
            translate.setText(translateResponse.text.get(0));
            translate.setFrom(from);
            translate.setTo(to);
            translate.setDictArticle(new DictArticle(dictResponse));
            return translate;
        });
    }
}
