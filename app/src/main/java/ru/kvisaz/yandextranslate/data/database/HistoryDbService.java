package ru.kvisaz.yandextranslate.data.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            HistoryEntity saved = getHistoryEntity(translate.getSource(), translate.getFrom(), translate.getTo());
            if (saved != null) {
                newEntity._id = saved._id;
            }
            Long savedId = getDatabaseCompartment().put(newEntity);
            return savedId;
        }).compose(applySchedulers());
    }

    public Observable<List<Translate>> fetchHistoryList() {
        return fetchHistoryList(false);
    }

    public Observable<List<Translate>> fetchFavorites() {
        return fetchHistoryList(true);
    }

    public Observable<List<Translate>> fetchHistoryList(boolean favoritesOnly) {

        DatabaseCompartment.QueryBuilder<HistoryEntity> queryBuilder;
        queryBuilder = favoritesOnly ? getFavoritesQueryBuilder() : getHistoryQueryBuilder();

        return Observable.fromCallable(queryBuilder::list)
                .map(this::getTranslates)
                .compose(applySchedulers());
    }


    public Observable<List<Translate>> fetchSearchLike(String source, boolean favoritesOnly) {
        DatabaseCompartment.QueryBuilder<HistoryEntity> qb = getHistoryQueryBuilder();
        String sourceLowCase = source.toLowerCase(Locale.getDefault()); // SQLite не позволяет case-insensitive для кириллицы
        final DatabaseCompartment.QueryBuilder<HistoryEntity> qb2 = favoritesOnly ? qb.withSelection("sourceLowCase LIKE ? AND isFavorite = ?", sourceLowCase + "%", "1")
                : getHistoryQueryBuilder().withSelection("sourceLowCase LIKE ?", sourceLowCase + "%");

        return Observable.fromCallable(() -> qb2.list())
                .map(this::getTranslates)
                .compose(applySchedulers());
    }

    public Observable<HistoryEntity> fetchSearchSame(String source, String from, String to) {
        return Observable.fromCallable(() -> {
            HistoryEntity historyEntity = getHistoryEntity(source, from, to);
            return historyEntity != null ? historyEntity : new HistoryEntity(); // Callable в Rx не может возвращать null, проверяем по _id
        }).compose(applySchedulers());
    }

    public Observable<Integer> delete(List<Translate> translatesForRemoving) {
        return Observable.fromCallable(() -> {
            StringBuilder sb = new StringBuilder("(");
            for (Translate translate : translatesForRemoving) {
                sb.append("'").append(translate.getSource()).append("'").append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // remove last
            sb.append(")");
            String str = sb.toString();
            return getDatabaseCompartment().delete(HistoryEntity.class, "source IN " + str);
        }).compose(applySchedulers());
    }


    private DatabaseCompartment.QueryBuilder<HistoryEntity> getEntityQuery() {
        return getDatabaseCompartment().query(HistoryEntity.class);
    }

    private DatabaseCompartment getDatabaseCompartment() {
        return cupboard().withDatabase(database);
    }

    private HistoryEntity getHistoryEntity(String source, String from, String to) {
        return getEntityQuery().withSelection("source = ? AND fromLang = ? AND toLang = ?", source, from, to).get();
    }

    private DatabaseCompartment.QueryBuilder<HistoryEntity> getHistoryQueryBuilder() {
        return getEntityQuery().orderBy("_id desc");
    }

    private DatabaseCompartment.QueryBuilder<HistoryEntity> getFavoritesQueryBuilder() {
        return getHistoryQueryBuilder().withSelection("isFavorite = ?", "1");
    }

    @NonNull
    private List<Translate> getTranslates(List<HistoryEntity> entities) {
        List<Translate> translateList = new ArrayList<>();
        for (HistoryEntity entity : entities) {
            translateList.add(new Translate(entity));
        }
        return translateList;
    }
}