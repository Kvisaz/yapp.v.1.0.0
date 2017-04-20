package ru.kvisaz.yandextranslate.screens.translator;

import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.data.rest.DictApi;
import ru.kvisaz.yandextranslate.data.rest.DictService;
import ru.kvisaz.yandextranslate.data.rest.TranslateService;
import ru.kvisaz.yandextranslate.di.ComponentProvider;

@InjectViewState
public class TranslatorPresenter extends MvpPresenter<ITranslatorView> implements ITranslatorPresenter {

    @Inject
    Handler handler;

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
    private DictArticle dictArticle;

    private Runnable fetchTranslateRunnable;


     /*
        *   TODO 4 Сохранение в БД статьи с ключом sourceText
    * */

     /*
     *        *
     *   todo BUG - ландскейп выглядит убого, не видна словарная статья
     * */


    public TranslatorPresenter() {
        super();
        ComponentProvider.getDataComponent().inject(this);

        sourceText = "";
        translatedText = "";
        languagesInfo = ActiveSession.getLanguages();
        sourceLangs = languagesInfo.getSourceLanguages();
        selectedSource = findSelectedSourceLanguage();
        destLangs = languagesInfo.getDestinations(selectedSource.code);
        selectedDestination = languagesInfo.getDefaultDestination(selectedSource);
    }

    @Override
    public void onStart() {
        getViewState().setSourceLanguages(sourceLangs);
        getViewState().selectSourceLanguage(selectedSource);
        getViewState().setDestinationLanguages(destLangs);
        if (selectedDestination != null) {
            getViewState().selectDestinationLanguage(selectedDestination);
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
    public void onInputChanged(String input) {
        // чистим от пробелов и лишних переводов
        String cleanedInput = cleanInput(input);
        if(sourceText.equals(cleanedInput)) return;

        // если редактирование продолжается, отменяем заказ на запрос к серверу, отправляем только когда набор закончился
        handler.removeCallbacks(fetchTranslateRunnable);

        // не обрабатываем пустые запросы
        if (cleanedInput.length() < Constants.MINIMAL_WORD_LENGTH) return;

        // обновляем поле
        sourceText = cleanedInput;

        // заказываем запрос к серверу, который исполнится, если набор закончен
        // (в течение некоторого времени не будет меняться текст)
        fetchTranslateRunnable = this::fetchTranslate;
        handler.postDelayed(fetchTranslateRunnable, Constants.DELAY_BETWEEN_INPUT_CHANGING_MS);
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

    private String cleanInput(String text) {
        return text.trim().replaceAll("[\n]{2,}", "\n");
    }

    private void fetchTranslate() {
        String from = selectedSource.code;
        String to = selectedDestination.code;

        // Запрос на перевод
        fetchYandexTranslateApi(sourceText, from, to);

        // Запрос на словарную статью
        fetchYandexDictApi(sourceText, from, to);
    }

    private void fetchYandexTranslateApi(String sourceText, String from, String to) {
        translateService.fetchTranslate(sourceText, from, to)
                .subscribe(
                        (translateResponse -> {
                            translatedText = translateResponse.text.get(0);
                            getViewState().showOriginalText(sourceText);
                            getViewState().showTranslatedText(translatedText);
                        }),
                        this::handleServerError);
    }

    private void fetchYandexDictApi(String sourceText, String from, String to) {
        dictService.fetchDefinition(sourceText, from, to, DictApi.LOOKUP_UI_DEFAULT_VALUE)
                .subscribe(
                        (dictResponse -> {
                            dictArticle = new DictArticle(dictResponse);
                            getViewState().showDictionaryArticle(dictArticle);
                        }),
                        this::handleServerError);
    }

    private void handleServerError(Throwable throwable) {
        Log.d(Constants.LOG_TAG, throwable.getMessage());
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
}