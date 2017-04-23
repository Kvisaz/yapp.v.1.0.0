package ru.kvisaz.yandextranslate.data.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import nl.qbusict.cupboard.DatabaseCompartment;
import ru.kvisaz.yandextranslate.common.RxService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.Translate;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class HistoryDbService extends RxService {

    private SQLiteDatabase database;

    public HistoryDbService(SQLiteDatabase database) {
        this.database = database;
    }

    public Observable<Long> save(Translate translate) {
        return Observable.fromCallable(() -> {
            HistoryEntity newEntity = new HistoryEntity(translate);
            HistoryEntity saved = getHistoryEntity(translate.getSource());
            if (saved != null) {
                newEntity._id = saved._id;
            }
            Long savedId = getDatabaseCompartment().put(newEntity);
            return savedId;
        }).compose(applySchedulers());
    }


    public Observable<List<Translate>> fetchHistory() {
        return Observable.fromCallable(() -> getEntityQuery()
                .orderBy("_id desc")
                .list())
                .map(entities -> {
                    List<Translate> translateList = new ArrayList<>();
                    for (HistoryEntity entity : entities) {
                        translateList.add(new Translate(entity));
                    }
                    return translateList;
                })
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchFavorites() {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("isFavorite = TRUE").list())
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchSearchLike(String source) {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("source LIKE ?", " " + source).list())
                .compose(applySchedulers());
    }

    public Observable<HistoryEntity> fetchSearchSame(String source) {
        return Observable.fromCallable(() -> {
            HistoryEntity historyEntity = getEntityQuery().withSelection("source = ?", source).get();
            return historyEntity != null ? historyEntity : new HistoryEntity(); // Callable в Rx не может возвращать null, проверяем по _id
        }).compose(applySchedulers());
    }


    private DatabaseCompartment.QueryBuilder<HistoryEntity> getEntityQuery() {
        return getDatabaseCompartment().query(HistoryEntity.class);
    }

    private DatabaseCompartment getDatabaseCompartment() {
        return cupboard().withDatabase(database);
    }

    private HistoryEntity getHistoryEntity(String sourceText) {
        return getEntityQuery().withSelection("source = ?", sourceText).get();
    }
}