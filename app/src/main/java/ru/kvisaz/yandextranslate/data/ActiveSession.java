package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.data.models.Translate;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;

public class ActiveSession {
    private static boolean mIsOnline;
    private static LanguagesInfo mLanguages;
    private static Translate mTranslate;

    public static boolean isOnline() {
        return mIsOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        ActiveSession.mIsOnline = isOnline;
    }

    public static void saveLanguageData(LanguagesResponse langResponse) {
        mLanguages = new LanguagesInfo(langResponse);
    }

    public static LanguagesInfo getLanguages() {
        return mLanguages;
    }

    public static Translate getTranslate() {
        return mTranslate;
    }

    public static void setTranslate(Translate mTranslate) {
        ActiveSession.mTranslate = mTranslate;
    }
}
