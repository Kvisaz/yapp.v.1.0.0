package ru.kvisaz.yandextranslate.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;

public class LanguagesInfo {
    private final LanguagesResponse mLanguagesResponse;

    public LanguagesInfo(LanguagesResponse languagesResponse) {
        mLanguagesResponse = languagesResponse;

        // todo delete
        Log.d(Constants.LOG_TAG, "dirs.size = " + mLanguagesResponse.dirs.size() + " langs.size = " + mLanguagesResponse.langs.size());
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

    public Language[] getDestinations(Language currentUserLanguage) {
        return new Language[0];
    }
}