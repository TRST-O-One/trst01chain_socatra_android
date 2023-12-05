package com.socatra.intellitrack.api.interceptors;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by satish on 15/3/17.
 */

public class RequestInterceptor implements Interceptor {

    private final ConnectivityManager connectivityManager;

    public RequestInterceptor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isConnected()) {
            throw new OfflineException();
        }

        Request.Builder r = chain.request().newBuilder();
        return chain.proceed(r.build());
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
