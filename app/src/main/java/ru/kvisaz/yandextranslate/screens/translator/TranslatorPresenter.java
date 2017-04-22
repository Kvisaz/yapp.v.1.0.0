package ru.kvisaz.yandextranslate.screens.translator;

import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.LocaleChecker;
import ru.kvisaz.yandextranslate.data.ActiveSession;
import ru.kvisaz.yandextranslate.data.TranslateRepository;
import ru.kvisaz.yandextranslate.data.UserSettings;
import ru.kvisaz.yandextranslate.data.database.HistoryDbService;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.data.rest.DictApi;
import ru.kvisaz.yandextranslate.data.rest.DictRestService;
import ru.kvisaz.yandextranslate.data.rest.TranslateRestService;
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
    TranslateRestService translateRestService;

    @Inject
    DictRestService dictRestService;

    @Inject
    HistoryDbService historyDbService;

    @Inject
    TranslateRepository translateRepository;

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
        *   TODO 1 IndexOutOfBoundsException: Invalid index 1, size is 1  при чтении слова Печень
        *   TODO 2  Сохранение запроса в базу данных после получения в Репозитории
        *   TODO 3
        *
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

        if (ActiveSession.isOnline()) {
            languagesInfo = ActiveSession.getLanguages();
            sourceLangs = languagesInfo.getSourceLanguages();
            selectedSource = findSelectedSourceLanguage();
            destLangs = languagesInfo.getDestinations(selectedSource.code);
            selectedDestination = languagesInfo.getDefaultDestination(selectedSource);
        }

    }

    @Override
    public void onStart() {
        getViewState().showOfflineScreen(!ActiveSession.isOnline());

        if (ActiveSession.isOnline()) {
            getViewState().setSourceLanguages(sourceLangs);
            getViewState().selectSourceLanguage(selectedSource);
            getViewState().setDestinationLanguages(destLangs);
            if (selectedDestination != null) {
                getViewState().selectDestinationLanguage(selectedDestination);
            }
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
        if (sourceText.equals(cleanedInput)) return;

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
    public void onOfflineButtonClick() {
        getViewState().goToStartActivity();
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

        // todo select UI in settings
        translateRepository.fetchTranslate(sourceText, from, to, DictApi.LOOKUP_UI_DEFAULT_VALUE)
                .subscribe(
                        (translate -> {
                            translatedText = translate.getText();
                            dictArticle = translate.getDictArticle();
                            getViewState().showOriginalText(sourceText);
                            getViewState().showTranslatedText(translatedText);
                            getViewState().showDictionaryArticle(dictArticle);

                            // todo to repo
                            historyDbService.save(translate).subscribe(
                                    (id -> {
                                        Log.d(Constants.LOG_TAG, "Saved id = " + id);
                                    }), this::handleServerError
                            );
                        }
                        ),
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