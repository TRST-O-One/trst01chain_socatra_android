package com.socatra.excutivechain.api.webservice;


//import static com.trst01.mvvm_room_java_template.api.webservice.AppWebService.apiBaseUrl;
//import static com.trst01.mvvm_room_java_template.api.webservice.AppWebService.apiBaseUrlVersion;

import static com.socatra.excutivechain.api.webservice.AppWebService.apiBaseUrl;
import static com.socatra.excutivechain.api.webservice.AppWebService.apiBaseUrlVersion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_funtion_class {

    private static Retrofit retrofit = null;
    private static OkHttpClient httpClient =  new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor())
            .build();
    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(apiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .client(httpClient)
//                    .client(httpClient)
                    .build();
        }
        return retrofit;
    } public static Retrofit getVersionClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(apiBaseUrlVersion)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

//    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
//
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//            return builder;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
