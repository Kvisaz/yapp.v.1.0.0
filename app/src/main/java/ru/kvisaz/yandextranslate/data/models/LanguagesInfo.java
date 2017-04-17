package ru.kvisaz.yandextranslate.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;

public class LanguagesInfo {
    private final LanguagesResponse mLanguagesResponse;

    public LanguagesInfo(LanguagesResponse languagesResponse) {
        mLanguagesResponse = languagesResponse;
    }

    @NonNull
    public Language[] getLanguages() {
        Language[] langDescriptions = new Language[mLanguagesResponse.langs.size()];
        int i = 0;
        for (Map.Entry<String, String> pair : mLanguagesResponse.langs.entrySet()) {
            langDescriptions[i] = new Language(pair.getKey(), pair.getValue());
            i++;
        }
        return langDescriptions;
    }

    @Nullable
    public Language getLanguage(String code) {
        Language language = null;
        if (mLanguagesResponse.langs.containsKey(code)) {
            String description = mLanguagesResponse.langs.get(code);
            language = new Language(code, description);
        }

        return language;
    }
}