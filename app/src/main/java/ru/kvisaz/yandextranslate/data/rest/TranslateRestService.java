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
import ru.kvisaz.yandextranslate.common.RxRestService;
import ru.kvisaz.yandextranslate.data.rest.models.LanguageDetectResponse;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;
import ru.kvisaz.yandextranslate.data.rest.models.TranslateResponse;

public class TranslateRestService extends RxRestService {

    private static final String DIRS_DELIMITER = Constants.DEFAULT_DIRECTION_DELIMITER;

    public interface IRest {
        @POST(TranslateApi.LANGUAGES_DATA_PATH)
        Observable<LanguagesResponse> fetchLanguages(@Query("ui") String ui);

        @POST(TranslateApi.DETECT_PATH)
        Observable<LanguageDetectResponse> detectLanguage(@Query("text") String text);

        @POST(TranslateApi.TRANSLATE_PATH)
        Observable<TranslateResponse> fetchTranslate(@Query("text") String text,
                                                     @Query("lang") String lang,
                                                     @Query("format") String format);

    }

    private IRest mApiRestService;

    @Inject
    public TranslateRestService(IRest apiRestService) {
        mApiRestService = apiRestService;
    }

    public Observable<LanguagesResponse> fetchLanguages(String languageUiCode) {
        return mApiRestService.fetchLanguages(languageUiCode).compose(applySchedulers());
    }

    public Observable<LanguageDetectResponse> detectLanguage(String text) {
        return mApiRestService.detectLanguage(text).compose(applySchedulers());
    }

    // answers in html format looks same as in plain format - 14.04.17
    public Observable<TranslateResponse> fetchTranslate(String text, String srcCode, String destCode) {
        return mApiRestService
                .fetchTranslate(text, buldLangParam(srcCode, destCode), TranslateApi.TRANSLATE_FORMAT_HTML_VALUE)
                .compose(applySchedulers());
    }

}
