package ru.kvisaz.yandextranslate.common;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.kvisaz.yandextranslate.BuildConfig;
import ru.kvisaz.yandextranslate.Constants;

public class RestInterceptor implements Interceptor {
    protected Map<String, String> mRequiredParamMap;

    public RestInterceptor(Map<String, String> requiredParamMap) {
        mRequiredParamMap = requiredParamMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl.Builder builder = originalHttpUrl.newBuilder();

        for (String paramName : mRequiredParamMap.keySet()) {
            String paramValue = mRequiredParamMap.get(paramName);
            builder.addQueryParameter(paramName, paramValue);
        }
        HttpUrl newUrl = builder.build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(newUrl);

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);

        if (BuildConfig.DEBUG) {
            response = traceResponseBody(request, response);
        }

        return response;
    }

    protected Response traceResponseBody(Request request, Response response) {
        if (response == null) {
            Log.d(Constants.LOG_TAG, "response == null");
            return response;
        }

        if (response.body() == null) {
            Log.d(Constants.LOG_TAG, "response.body()==null");
            return response;
        }

        try {
            String bodyString = response.body().string();
            Log.d(Constants.LOG_TAG, String.format("Sending request %s with %s", request.url(), request.headers()));
            Log.d(Constants.LOG_TAG, bodyString);

            // we need build response again because response.body().string() eliminated response body
            MediaType contentType = response.body().contentType();
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, e.getMessage());
            return response;
        }
    }
}
