package com.sanyuelanv.sanwebapp.utils;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create By songhang in 2020/3/30
 */
public class SanYueNetCacheInterceptor implements Interceptor {
    private int maxCacheSeconds;

    public SanYueNetCacheInterceptor(int maxCacheSeconds) {
        this.maxCacheSeconds = maxCacheSeconds;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        request = builder.build();
        Response originalResponse = chain.proceed(request);
        return  originalResponse;
//        return originalResponse.newBuilder()
//                .header("Cache-Control", "no-cache , max-age=" + maxCacheSeconds)
//                .build();
    }
}
