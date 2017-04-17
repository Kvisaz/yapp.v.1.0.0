package ru.kvisaz.yandextranslate.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.rest.models.Languages;

public class ActiveSession {
    private static Languages mLanguages;

    @NonNull
    public static Language[] getLanguageDescriptions() {
        Language[] langDescriptions = new Language[mLanguages.langs.size()];
        int i = 0;
        for (Map.Entry<String,String> pair: mLanguages.langs.entrySet()) {
            langDescriptions[i] = new Language(pair.getKey(), pair.getValue());
            i++;
        }
        return  langDescriptions;
    }

    // en, ru
    @Nullable
    public static Language getLanguageByCode(String code){
        Language language = null;
        if(mLanguages.langs.containsKey(code)){
             String description = mLanguages.langs.get(code);
             language = new Language(code, description);
        }

        return language;
    }

    public static void setLanguages(Languages languages) {
        mLanguages = languages;
    }
}
