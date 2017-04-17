package ru.kvisaz.yandextranslate.di;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.common.ConnectivityChecker;
import ru.kvisaz.yandextranslate.data.rest.YandexService;
import ru.kvisaz.yandextranslate.data.rest.YandexTranslateInterceptor;

@Module
public class NetworkModule {
    private final static String DEFAULT_HTTP_CLIENT = "DEFAULT_HTTP_CLIENT";
    private final static String DEFAULT_RETROFIT = "DEFAULT_RETROFIT";
    private final static String DEFAULT_API_REST = "DEFAULT_API_REST";
    private final static String DEFAULT_API_SERVICE = "DEFAULT_API_SERVICE";

    @Provides
    @Singleton
    ConnectivityChecker providesConnectivityChecker(Context context){
        final ConnectivityChecker connectivityChecker = new ConnectivityChecker(context);
        return connectivityChecker;
    }

    @Provides
    @Singleton
    @Named(DEFAULT_HTTP_CLIENT)
    OkHttpClient providesYandexTranslateHTTPClient(){
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new YandexTranslateInterceptor())
                .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS,TimeUnit.SECONDS)
                .build();
        return client;
    }

    @Provides
    @Singleton
    @Named(DEFAULT_RETROFIT)
    Retrofit providesYandexTranslateRetrofit(@Named(DEFAULT_HTTP_CLIENT) OkHttpClient client){
        final Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    YandexService.IRest providesYandexApiRest(@Named(DEFAULT_RETROFIT) Retrofit retrofit){
        return retrofit.create(YandexService.IRest.class);
    }

    @Provides
    @Singleton
    @Named(DEFAULT_API_SERVICE)
    YandexService providesYandexApiService(YandexService.IRest rest){
        return new YandexService(rest);
    }
}
