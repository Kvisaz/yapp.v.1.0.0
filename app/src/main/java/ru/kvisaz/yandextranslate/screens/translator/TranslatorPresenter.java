package ru.kvisaz.yandextranslate.screens.translator;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.data.rest.DictApi;
import ru.kvisaz.yandextranslate.data.rest.DictService;
import ru.kvisaz.yandextranslate.data.rest.TranslateService;
import ru.kvisaz.yandextranslate.data.rest.models.DictDef;
import ru.kvisaz.yandextranslate.data.rest.models.DictResponse;
import ru.kvisaz.yandextranslate.data.rest.models.TranslateResponse;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class TranslatorPresenter extends MvpPresenter<ITranslatorView> implements ITranslatorPresenter {

    @Inject
    Context mContext;

    @Inject
    LocaleChecker localeChecker;

    @Inject
    UserSettings userSettings;

    @Inject
    TranslateService translateService;

    @Inject
    DictService dictService;

    private LanguagesInfo languagesInfo;
    private Language selectedSource;
    private Language selectedDestination;
    private Language[] sourceLangs;
    private Language[] destLangs;

    private String sourceText;
    private String translatedText;
    private String dictionaryDefinition;


    private Disposable translateDisposable;

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
        if (selectedDestination != null) {
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
        if (sourceLanguage.equals(selectedSource)) return; // stop call when select source

        Language tempSelectedSource = selectedSource;
        selectedSource = sourceLanguage;
        destLangs = languagesInfo.getDestinations(selectedSource.code);
        selectedDestination = tempSelectedSource;

        getViewState().setDestinationLanguages(destLangs);
        getViewState().selectDestinationLanguage(selectedDestination);
    }

    @Override
    public void onDestinationSelect(Language destinationLanguage) {
        if (destinationLanguage.equals(selectedDestination))
            return; // stop call when select destination
        selectedDestination = destinationLanguage;
    }

    @Override
    public void onSwitchLanguagesButtonClick() {
        Language tempSelectedSource = selectedSource;
        selectedSource = selectedDestination;
        selectedDestination = tempSelectedSource;

        destLangs = languagesInfo.getDestinations(selectedSource.code);

        getViewState().selectSourceLanguage(selectedSource);
        getViewState().setDestinationLanguages(destLangs);
        getViewState().selectDestinationLanguage(selectedDestination);
    }

    @Override
    public void onInputChanged(Editable editable) {
        if (editable.length() < Constants.MINIMAL_WORD_LENGTH) {
            cancelPreviousInputChange(); // отменяем предыдущий запрос к серверу, продолжается редактирование
            return;
        }
        sourceText = editable.toString();
        fetchTranslate();
    }

    /*
    *   TODO 1 Сделать общий троттлинг ввода клавиатуры через Handler - так проще
    *   TODO 2 Сделать параллельными запросы к переводу и словарю
    *   TODO 3 БОЛЬШОЕ сделать форматирование вывода словаря (через HTML видимо)    *
    *
    * */

    private void fetchTranslate() {
        /*
        *   Не посылаем запрос к серверу, пока не пройдет DELAY_BETWEEN_INPUT_CHANGING_MS
        *   отменяем
        * */
        cancelPreviousInputChange();
        String from = selectedSource.code;
        String to = selectedDestination.code;

        Observable<TranslateResponse> translateResponseObservable = translateService.fetchTranslate(sourceText, from, to);
        Observable<DictResponse> dictDefObservable = dictService.fetchDefinition(sourceText, from, to, DictApi.LOOKUP_UI_DEFAULT_VALUE);

        translateDisposable = Observable.just(1).delay(Constants.DELAY_BETWEEN_INPUT_CHANGING_MS, TimeUnit.MILLISECONDS)
                .flatMap(delay -> Observable.zip(translateResponseObservable, dictDefObservable, ((translateResponse, dictDef) -> new CombinedResponse(translateResponse, dictDef))))
                .subscribe((combinedResponse -> {
                            translatedText = combinedResponse.translateResponse.text.get(0);
                            dictionaryDefinition = combinedResponse.dictResponse.def.get(0).text;
                            getViewState().showOriginalText(sourceText);
                            getViewState().showTranslatedText(translatedText);
                            getViewState().showDictionaryText(getSpannedFromString(dictionaryDefinition));
                        }),
                        throwable -> {
                            Log.d(Constants.LOG_TAG, throwable.getMessage());
                        });
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
        if (sourceLanguage == null) {
            sourceLanguage = getLanguageFromCurrentLocale();
            if (!languagesInfo.hasSource(sourceLanguage)) {
                sourceLanguage = languagesInfo.getDefaultSourceLanguage();
            }
        }
        return sourceLanguage;
    }

    private Language getLanguageFromCurrentLocale() {
        String languageCode = localeChecker.getLanguageCode();
        return ActiveSession.getLanguages().getLanguage(languageCode);
    }

    private void cancelPreviousInputChange() {
        if (translateDisposable != null) {
            translateDisposable.dispose();
        }
    }

    private Spanned getSpannedFromString(String code) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(code, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spanned = Html.fromHtml(code);
        }
        return spanned;
    }

    private class CombinedResponse {
        public final TranslateResponse translateResponse;
        public final DictResponse dictResponse;

        public CombinedResponse(TranslateResponse translateResponse, DictResponse dictResponse) {
            this.translateResponse = translateResponse;
            this.dictResponse = dictResponse;
        }
    }

}