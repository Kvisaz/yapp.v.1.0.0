package ru.kvisaz.yandextranslate.data.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import io.reactivex.Observable;
import nl.qbusict.cupboard.DatabaseCompartment;
import ru.kvisaz.yandextranslate.common.RxService;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Translate;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class HistoryDbService extends RxService {

    private SQLiteDatabase database;

    public HistoryDbService(SQLiteDatabase database) {
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

    public Observable<Long> save(Translate translate) {
        return Observable.fromCallable(() -> {
            HistoryEntity newEntity = new HistoryEntity(translate.getSource(), translate.getText(), translate.getFrom(), translate.getTo());
            newEntity.setWordType(translate.getDictArticle().type);
            newEntity.setSynonims(translate.getDictArticle().synonimStrings);
            newEntity.setMeans(translate.getDictArticle().meanStrings);

            HistoryEntity saved = getHistoryEntity(translate.getSource());
            if (saved != null) {
                newEntity._id = saved._id;
            }
            Long savedId = getDatabaseCompartment().put(newEntity);
            return savedId;
        }).compose(applySchedulers());
    }


    public Observable<List<HistoryEntity>> fetchHistory() {
        return Observable.fromCallable(() -> getEntityQuery()
                .orderBy("_id desc")
                .list())
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchFavorites() {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("isFavorite = ?", "TRUE").list())
                .compose(applySchedulers());
    }

    public Observable<List<HistoryEntity>> fetchSearchLike(String source) {
        return Observable.fromCallable(() -> getEntityQuery()
                .withSelection("source = ?", "LIKE " + source).list())
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