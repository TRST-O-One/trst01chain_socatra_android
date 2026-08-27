package com.socatra.excutivechain.api.webservice;


import static com.socatra.excutivechain.utils.AppConstant.BASE_AUTH_URL;

import com.socatra.excutivechain.api.interceptors.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppWebService {

    public static String apiBaseUrl = BASE_AUTH_URL;
    public static String apiBaseUrlVersion = BASE_AUTH_URL;

    private static OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(apiBaseUrl)
            .client(httpClient);

    public static void changeApiBaseUrl(String newBASE_URL) {
        apiBaseUrl = newBASE_URL;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .baseUrl(apiBaseUrl)
                .client(httpClient);
    }

    public static <S> S createService(Class<S> serviceClass) {
        return builder.build().create(serviceClass);
    }
}
