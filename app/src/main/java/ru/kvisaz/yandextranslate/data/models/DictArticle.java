package ru.kvisaz.yandextranslate.data.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.kvisaz.yandextranslate.common.utils.StringUtils;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.rest.models.DictDef;
import ru.kvisaz.yandextranslate.data.rest.models.DictMean;
import ru.kvisaz.yandextranslate.data.rest.models.DictResponse;
import ru.kvisaz.yandextranslate.data.rest.models.DictSyn;
import ru.kvisaz.yandextranslate.data.rest.models.DictTr;

public class DictArticle {
    private final static String WORD_DELIMITER = ", ";

    public final String text;
    public final String type;
    public final List<String> synonimStrings = new ArrayList<>();
    public final List<String> meanStrings = new ArrayList<>();

    public final boolean isEmpty;

    public DictArticle() {
        isEmpty = true;
        text = StringUtils.EMPTY_STRING;
        type = StringUtils.EMPTY_STRING;
    }

    public DictArticle(DictResponse dictResponse) {
        isEmpty = dictResponse.def == null || dictResponse.def.size() == 0;
        /*
        *     Может ли быть у нас пустой ответ словаря в правильном запросе?
        *     Да, если направление в переводчике есть, а в словаре - нет.
        *     Пример Русский -> Норвежский
        *     Поэтому нужно вернуть пустую статью
        *
        * */
        if (isEmpty) {
            text = StringUtils.EMPTY_STRING;
            type = StringUtils.EMPTY_STRING;
            return;
        }

        DictDef firstDefinition = dictResponse.def.get(0);
        text = StringUtils.getEmptyIfNull(firstDefinition.text);
        type = StringUtils.getEmptyIfNull(firstDefinition.pos);

        if (firstDefinition.tr == null || firstDefinition.tr.size() == 0) {
            return;
        }

        for (DictTr dictTranslate : firstDefinition.tr) {
            synonimStrings.add(getSynonims(dictTranslate));
            meanStrings.add(getMean(dictTranslate));
        }
    }

    public DictArticle(@NonNull HistoryEntity historyEntity) {
        isEmpty = false;
        text = historyEntity.source;
        type = historyEntity.type;
        synonimStrings.addAll(historyEntity.getSynonims());
        meanStrings.addAll(historyEntity.getMeans());
    }


    private String getSynonims(DictTr translate) {
        if (translate == null) return StringUtils.EMPTY_STRING;
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.getEmptyIfNull(translate.text));

        if (translate.syn != null) {
            for (DictSyn syn : translate.syn) {
                sb.append(WORD_DELIMITER).append(StringUtils.getEmptyIfNull(syn.text));
            }
        }

        return sb.toString();
    }

    private String getMean(DictTr translate) {
        if (translate == null || translate.mean == null || translate.mean.size() == 0) {
            return StringUtils.EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;
        for (DictMean mean : translate.mean) {
            if (!isFirst) {
                sb.append(WORD_DELIMITER);
            }
            if (isFirst) {
                isFirst = false;
            }
            sb.append(StringUtils.getEmptyIfNull(mean.text));
        }

        return sb.toString();
    }
}