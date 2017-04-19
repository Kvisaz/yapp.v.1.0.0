package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.models.Language;

public class UserSettings {
    private Language mLanguage;
    private String mLanguageCode;

    public Language getLanguage() {
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