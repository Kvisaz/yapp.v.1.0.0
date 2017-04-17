package ru.kvisaz.yandextranslate.data.rest;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;
import ru.kvisaz.yandextranslate.data.rest.models.LanguageDetectResponse;
import ru.kvisaz.yandextranslate.data.rest.models.TranslateResponse;

public class YandexService {

    public interface IRest {
        @POST(Constants.API_LANGUAGES_DATA_PATH)
        Observable<LanguagesResponse> fetchLanguages(@Query(Constants.API_LANGUAGES_UI_PARAM) String ui);

        @POST(Constants.API_DETECT_PATH)
        Observable<LanguageDetectResponse> detectLanguage(@Query(Constants.API_DETECT_TEXT_PARAM) String text);

        @POST(Constants.API_TRANSLATE_PATH)
        Observable<TranslateResponse> fetchTranslate(@Query(Constants.API_TRANSLATE_TEXT_PARAM) String text,
                                                     @Query(Constants.API_TRANSLATE_LANG_PARAM) String lang,
                                                     @Query(Constants.API_TRANSLATE_FORMAT_PARAM) String format);

    }

    // ===========================================================
    // Fields
    // ===========================================================

    private IRest mApiRestService;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Inject
    public YandexService(IRest apiRestService){
        mApiRestService = apiRestService;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public Observable<LanguagesResponse> fetchLanguages(String languageUiCode){
        return mApiRestService.fetchLanguages(languageUiCode).compose(applySchedulers());
    }

    public Observable<LanguageDetectResponse> detectLanguage(String text){
        return mApiRestService.detectLanguage(text).compose(applySchedulers());
    }

    // answers in html format looks same as in plain format - 14.04.17
    public Observable<TranslateResponse> fetchTranslate(String text, String lang){
        return mApiRestService.fetchTranslate(text, lang, Constants.API_TRANSLATE_FORMAT_HTML_VALUE).compose(applySchedulers());
    }

    // ===========================================================
    // Private Methods
    // ===========================================================
    private <T>ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
