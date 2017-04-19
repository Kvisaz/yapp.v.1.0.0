package ru.kvisaz.yandextranslate.screens.translator;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Locale;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class TranslatorPresenter extends MvpPresenter<ITranslatorView> implements ITranslatorPresenter {

    @Inject
    Context mContext;

    @Inject
    LocaleChecker localeChecker;

    public TranslatorPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onStart() {
        LanguagesInfo languagesInfo = ActiveSession.getLanguages();
        Language[] languagesArray = languagesInfo.getLanguages();
        getViewState().setSourceLanguages(languagesArray);

        String languageCode = localeChecker.getLanguageCode();
        Language currentUserLanguage = ActiveSession.getLanguages().getLanguage(languageCode);
        if (currentUserLanguage != null) {
            getViewState().selectSourceLanguage(currentUserLanguage);
        }

        Language[] destinationLanguageArray = languagesInfo.getDestinations(currentUserLanguage);
        getViewState().setDestinationLanguages(destinationLanguageArray);
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

    @Override
    public void onSourceSelect(Language sourceLanguage) {

    }

    @Override
    public void onDestinationSelect(Language destinationLanguage) {

    }

    @Override
    public void onSwitchLanguagesButtonClick() {

    }

    @Override
    public void onMakeFavoriteCheck(Boolean checked) {

    }

    @Override
    public void onSourceSoundButtonClick() {

    }

    @Override
    public void onDestinationSoundButtonClick() {

    }

    @Override
    public void onShareButtonClick() {

    }

    @Override
    public void onCopyButtonClick() {

    }
}
