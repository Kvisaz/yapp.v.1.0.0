package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.models.Language;

public class UserSettings {
    private Language mLanguage;
    private String mLanguageCode;

    public Language getLanguage() {
        if (mLanguage == null) {
            mLanguage = new Language(Constants.DEFAULT_LANGUAGE_CODE, Constants.DEFAULT_LANGUAGE_DESCRIPTION);
        }
        return mLanguage;
    }

    public Language getDefaultLanguage() {
        if (mLanguage == null) {
            mLanguage = new Language(Constants.DEFAULT_LANGUAGE_CODE, Constants.DEFAULT_LANGUAGE_DESCRIPTION);
        }
        return mLanguage;
    }

    public void setLanguage(Language language) {
        this.mLanguage = language;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.mLanguageCode = languageCode;
    }
}