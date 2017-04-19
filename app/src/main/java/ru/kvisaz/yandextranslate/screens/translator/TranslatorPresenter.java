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
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class TranslatorPresenter extends MvpPresenter<ITranslatorView> implements ITranslatorPresenter {

    @Inject
    Context mContext;

    @Inject
    LocaleChecker localeChecker;

    @Inject
    UserSettings userSettings;

    private LanguagesInfo languagesInfo;
    private Language selectedSource;
    private Language selectedDestination;
    private Language[] sourceLangs;
    private Language[] destLangs;

    public TranslatorPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);
    }

    @Override
    public void onStart() {
        languagesInfo = ActiveSession.getLanguages();
        sourceLangs = languagesInfo.getSourceLanguages();
        selectedSource = findSelectedSourceLanguage();
        destLangs = languagesInfo.getDestinations(selectedSource.code);
        selectedDestination = languagesInfo.getDefaultDestination(selectedSource);

        getViewState().setSourceLanguages(sourceLangs);
        getViewState().selectSourceLanguage(selectedSource);
        getViewState().setDestinationLanguages(destLangs);
        if(selectedDestination!=null){
            getViewState().selectDestinationLanguage(selectedDestination);
        }
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
        if(sourceLanguage.equals(selectedSource)) return; // stop call when select source

        Language tempSelectedSource = selectedSource;
        selectedSource = sourceLanguage;
        destLangs = languagesInfo.getDestinations(selectedSource.code);
        selectedDestination = tempSelectedSource;

        getViewState().setDestinationLanguages(destLangs);
        getViewState().selectDestinationLanguage(selectedDestination);
    }

    @Override
    public void onDestinationSelect(Language destinationLanguage) {
        if(destinationLanguage.equals(selectedDestination)) return; // stop call when select destination
        selectedDestination = destinationLanguage;
    }

    @Override
    public void onSwitchLanguagesButtonClick() {
        Language tempSelectedSource = selectedSource;
        selectedSource = selectedDestination;
        selectedDestination = tempSelectedSource;

        destLangs = languagesInfo.getDestinations(selectedSource.code);

        getViewState().selectDestinationLanguage(selectedSource);
        getViewState().setDestinationLanguages(destLangs);
        getViewState().selectDestinationLanguage(selectedDestination);
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

    private Language findSelectedSourceLanguage() {
        Language sourceLanguage = userSettings.getLanguage();
        if(sourceLanguage == null){
            sourceLanguage =  getLanguageFromCurrentLocale();
            if(!languagesInfo.hasSource(sourceLanguage)){
                sourceLanguage = languagesInfo.getDefaultSourceLanguage();
            }
        }
        return sourceLanguage;
    }

    private Language getLanguageFromCurrentLocale() {
        String languageCode = localeChecker.getLanguageCode();
        return ActiveSession.getLanguages().getLanguage(languageCode);
    }

}