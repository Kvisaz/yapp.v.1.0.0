package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.models.Language;

public class UserSettings {
    private Language language;

    public Language getLanguage() {
        if (language == null) {
            language = new Language(Constants.DEFAULT_LANGUAGE_CODE, Constants.DEFAULT_LANGUAGE_DESCRIPTION);
        }
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}