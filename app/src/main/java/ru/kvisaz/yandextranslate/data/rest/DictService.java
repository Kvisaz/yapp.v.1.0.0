package ru.kvisaz.yandextranslate.data.rest;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.kvisaz.yandextranslate.common.RxRestService;
import ru.kvisaz.yandextranslate.data.rest.models.DictDef;

public class DictService extends RxRestService {

    public interface IRest {
        @POST(DictApi.LOOKUP_PATH)
        Observable<DictDef> fetchDefinition(@Query("text") String text,
                                            @Query("lang") String lang,
                                            @Query("ui") String ui);
    }

    private IRest mRestService;

    @Inject
    public DictService(IRest rest) {
        mRestService = rest;
    }

    public Observable<DictDef> fetchDefinition(String text, String from, String to, String ui) {
        return mRestService.fetchDefinition(text, buldLangParam(from, to), ui).compose(applySchedulers());
    }
}