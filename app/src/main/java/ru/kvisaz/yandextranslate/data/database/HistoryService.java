package ru.kvisaz.yandextranslate.data.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import io.reactivex.Observable;
import nl.qbusict.cupboard.DatabaseCompartment;
import ru.kvisaz.yandextranslate.common.RxService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.DictArticle;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class HistoryService extends RxService {

    private SQLiteDatabase database;

    public HistoryService(SQLiteDatabase database) {
        this.database = database;
    }

    public Observable<Long> save(HistoryEntity entity) {
        return Observable.fromCallable(() -> {
            Long savedId;
            HistoryEntity saved = getHistoryEntity(entity.source);
            if (saved == null) {
                saved = entity;
            } else {
                saved.copy(entity);
            }
            savedId = getDatabaseCompartment().put(saved);
            return savedId;
        }).compose(applySchedulers());
    }

    public Observable<Long> save(String sourceText, String translatedText, String from, String to) {
        return Observable.fromCallable(() -> {
            Long savedId;
            HistoryEntity newEntity = new HistoryEntity(sourceText, translatedText, from, to);
            HistoryEntity saved = getHistoryEntity(sourceText);
            if (saved == null) {
                saved = newEntity;
            } else {
                saved.copy(newEntity);
            }
            savedId = getDatabaseCompartment().put(saved);
            return savedId;
        }).compose(applySchedulers());
    }

    public Observable<Long> save(String sourceText, DictArticle dictArticle) {
        return Observable.fromCallable(() -> {
            HistoryEntity saved = getHistoryEntity(sourceText);
            if (saved == null) {
                throw new Exception("No HistoryEntity with this source text");
            } else {
                saved.setWordType(dictArticle.type);
                saved.setSynonims(dictArticle.synonimStrings);
                saved.setMeans(dictArticle.meanStrings);
            }
            return getDatabaseCompartment().put(saved);
        }).compose(applySchedulers());
    }



    public Observable<List<HistoryEntity>> fetchHistory() {
        return Observable.fromCallable(() -> getEntityQuery().list())
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchFavorites() {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("isFavorite = ?", "TRUE").list())
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchSearchBy(String source) {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("source = ?", "LIKE " + source).list())
                .compose(applySchedulers());
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