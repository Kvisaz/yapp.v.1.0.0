package ru.kvisaz.yandextranslate.data.database.models;

import java.util.Arrays;
import java.util.List;

import ru.kvisaz.yandextranslate.common.utils.StringUtils;
import ru.kvisaz.yandextranslate.data.database.DbConstants;
import ru.kvisaz.yandextranslate.data.models.Translate;

public class HistoryEntity {
    public Long _id;
    public Boolean isFavorite;
    public String fromLang; // en
    public String toLang; // ru
    public String source;
    public String translated;
    public String type;
    public String synonimStringsEncoded;
    public String meanStringsEncoded;

    public HistoryEntity() {
        isFavorite = false;
        fromLang = "";
        toLang = "";
        source = "";
        translated = "";
        type = "";
        synonimStringsEncoded = "";
        meanStringsEncoded = "";
    }

    public HistoryEntity(Translate translate){
        isFavorite = translate.isFavorite();
        fromLang = translate.getFrom();
        toLang = translate.getTo();
        source = translate.getSource();
        translated = translate.getText();
        type = translate.getDictArticle().type;
        setSynonims(translate.getDictArticle().synonimStrings);
        setMeans(translate.getDictArticle().meanStrings);
    }

    public HistoryEntity(String source, String translate, String from, String to) {
        this();
        this.fromLang = from;
        this.toLang = to;
        this.source = source;
        this.translated = translate;
    }

    public void copy(HistoryEntity historyEntity) {
        isFavorite = historyEntity.isFavorite;
        fromLang = historyEntity.fromLang;
        toLang = historyEntity.toLang;
        source = historyEntity.source;
        translated = historyEntity.translated;
        type = historyEntity.type;
        synonimStringsEncoded = historyEntity.synonimStringsEncoded;
        meanStringsEncoded = historyEntity.meanStringsEncoded;
    }

    public void setWordType(String type) {
        this.type = type;
    }

    public String getWordType() {
        return this.type;
    }

    public List<String> getSynonims() {
        return Arrays.asList(synonimStringsEncoded.split(DbConstants.DEFAULT_DELIMITER));
    }

    public List<String> getMeans() {
        return Arrays.asList(meanStringsEncoded.split(DbConstants.DEFAULT_DELIMITER));
    }

    public void setSynonims(List<String> syns) {
        synonimStringsEncoded = StringUtils.joinToString(syns, DbConstants.DEFAULT_DELIMITER);
    }

    public void setMeans(List<String> means) {
        meanStringsEncoded = StringUtils.joinToString(means, DbConstants.DEFAULT_DELIMITER);
    }

}