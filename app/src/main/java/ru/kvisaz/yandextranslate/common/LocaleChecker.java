package ru.kvisaz.yandextranslate.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class LocaleChecker {
    private Context mContext;

    public LocaleChecker(Context context) {
        mContext = context;
    }

    public String getLanguageCode() {
        Locale currentLocale = getCurrentLocale();
        return currentLocale.getLanguage();
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return mContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return mContext.getResources().getConfiguration().locale;
        }
    }

}
