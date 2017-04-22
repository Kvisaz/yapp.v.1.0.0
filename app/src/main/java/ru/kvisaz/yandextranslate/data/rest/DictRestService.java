package ru.kvisaz.yandextranslate.data.rest;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.kvisaz.yandextranslate.common.RxRestService;
import ru.kvisaz.yandextranslate.data.rest.models.DictDef;
import ru.kvisaz.yandextranslate.data.rest.models.DictResponse;

public class DictRestService extends RxRestService {

    public interface IRest {
        @POST(DictApi.LOOKUP_PATH)
        Observable<DictResponse> fetchDefinition(@Query("text") String text,
                                            @Query("lang") String lang,
                                            @Query("ui") String ui);
    }

    private IRest mRestService;

    @Inject
    public DictRestService(IRest rest) {
        mRestService = rest;
    }

    public Observable<DictResponse> fetchDefinition(String text, String from, String to, String ui) {
        return mRestService.fetchDefinition(text, buldLangParam(from, to), ui).compose(applySchedulers());
    }
}