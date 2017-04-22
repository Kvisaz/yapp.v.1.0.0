package ru.kvisaz.yandextranslate.di;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.kvisaz.yandextranslate.ApiKeys;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.ConnectivityChecker;
import ru.kvisaz.yandextranslate.common.RestInterceptor;
import ru.kvisaz.yandextranslate.data.rest.DictApi;
import ru.kvisaz.yandextranslate.data.rest.DictRestService;
import ru.kvisaz.yandextranslate.data.rest.TranslateApi;
import ru.kvisaz.yandextranslate.data.rest.TranslateRestService;

@Module
public class NetworkModule {
    private final static String TRANSLATE_HTTP_CLIENT = "TRANSLATE_HTTP_CLIENT";
    private final static String TRANSLATE_RETROFIT = "TRANSLATE_RETROFIT";


    private final static String DICT_HTTP_CLIENT = "DICT_HTTP_CLIENT";
    private final static String DICT_RETROFIT = "DICT_RETROFIT";

    @Provides
    @Singleton
    ConnectivityChecker providesConnectivityChecker(Context context) {
        final ConnectivityChecker connectivityChecker = new ConnectivityChecker(context);
        return connectivityChecker;
    }

    @Provides
    @Singleton
    @Named(TRANSLATE_HTTP_CLIENT)
    OkHttpClient providesYandexTranslateHTTPClient() {
        Map<String, String> params = new HashMap<>();
        params.put(TranslateApi.KEY_PARAM, ApiKeys.API_LANGUAGES_KEY_VALUE);
        final OkHttpClient client = getOkHttpClient(params);
        return client;
    }

    @Provides
    @Singleton
    @Named(DICT_HTTP_CLIENT)
    OkHttpClient providesDictHTTPClient() {
        Map<String, String> params = new HashMap<>();
        params.put(DictApi.KEY_PARAM, ApiKeys.API_DICT_KEY_VALUE);
        final OkHttpClient client = getOkHttpClient(params);
        return client;
    }

    @NonNull
    private OkHttpClient getOkHttpClient(Map<String, String> params) {
        return new OkHttpClient.Builder()
                .addInterceptor(new RestInterceptor(params))
                .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }


    @Provides
    @Singleton
    @Named(TRANSLATE_RETROFIT)
    Retrofit providesYandexTranslateRetrofit(@Named(TRANSLATE_HTTP_CLIENT) OkHttpClient client) {
        final Retrofit retrofit = getRetrofit(client, TranslateApi.BASE_URL);
        return retrofit;
    }

    @Provides
    @Singleton
    @Named(DICT_RETROFIT)
    Retrofit providesDictRetrofit(@Named(DICT_HTTP_CLIENT) OkHttpClient client) {
        final Retrofit retrofit = getRetrofit(client, DictApi.BASE_URL);
        return retrofit;
    }


    @NonNull
    private Retrofit getRetrofit(@Named(TRANSLATE_HTTP_CLIENT) OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    TranslateRestService.IRest providesYandexApiRest(@Named(TRANSLATE_RETROFIT) Retrofit retrofit) {
        return retrofit.create(TranslateRestService.IRest.class);
    }

    @Provides
    @Singleton
    TranslateRestService providesYandexApiService(TranslateRestService.IRest rest) {
        return new TranslateRestService(rest);
    }

    @Provides
    @Singleton
    DictRestService.IRest providesDictApiRest(@Named(DICT_RETROFIT) Retrofit retrofit) {
        return retrofit.create(DictRestService.IRest.class);
    }

    @Provides
    @Singleton
    DictRestService providesDictApiService(DictRestService.IRest rest) {
        return new DictRestService(rest);
    }
}