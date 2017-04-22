package ru.kvisaz.yandextranslate.data.models;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;

public class Translate {
    public final boolean isNew;

    private boolean mIsFavorite;
    private String mSource;
    private String mTranslate;
    private String mFrom;
    private String mTo;
    private DictArticle mDictArticle;

    public Translate() {
        isNew = true;
    }

    public Translate(HistoryEntity historyEntity) {
        isNew = false;
        mIsFavorite = historyEntity.isFavorite;
        mSource = historyEntity.source;
        mTranslate = historyEntity.translated;
        mFrom = historyEntity.fromLang;
        mTo = historyEntity.toLang;
        mDictArticle = new DictArticle(historyEntity);
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public String getText() {
        return mTranslate;
    }

    public void setText(String mText) {
        this.mTranslate = mText;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public void setTo(String mTo) {
        this.mTo = mTo;
    }

    public DictArticle getDictArticle() {
        return mDictArticle;
    }

    public void setDictArticle(DictArticle mDictArticle) {
        this.mDictArticle = mDictArticle;
    }
}