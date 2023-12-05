package com.socatra.excutivechain.api.webservice;




import static com.socatra.excutivechain.AppConstant.BASE_AUTH_URL;

import com.socatra.excutivechain.api.interceptors.DownloadOkHtttpClient;
import com.socatra.excutivechain.api.interceptors.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppWebService {

    public static String apiBaseUrl = BASE_AUTH_URL;
//    public static String apiBaseUrlVersion = BASE_AUTH_URL;
    public static String apiBaseUrlVersion = BASE_AUTH_URL;

    private static OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    //    private static OkHttpClient httpClient = new OkHttpClient();
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

    public static void changeDownloadBaseUrl(String newBASE_URL,
                                             DownloadOkHtttpClient.ProgressListener progressListener) {
        OkHttpClient downloadHttpClient = DownloadOkHtttpClient.getDownloadOkHttpClient(progressListener);
        apiBaseUrl = newBASE_URL;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .baseUrl(apiBaseUrl)
                .client(downloadHttpClient);
    }

    public static <S> S createService(Class<S> serviceClass) {
        return builder.build().create(serviceClass);
    }


}


//    //    private static OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
//    private static OkHttpClient httpClient =  new OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(new LoggingInterceptor())
//            .build();
//    private static Gson gson = new GsonBuilder()
//            .setLenient()
////            .setPrettyPrinting()
//            .excludeFieldsWithoutExposeAnnotation()
//            .create();
//
//    // httpClient.interceptors().add(new LoggingInterceptor());
//    OkHttpClient.Builder httpClient_new = new OkHttpClient.Builder();
//    //   httpClient.addInterceptor(new void LogJsonInterceptor());
////    private static OkHttpClient httpClient = new OkHttpClient();
////    private static OkHttpClient httpClient = new OkHttpClient();
//
//
//    private static Retrofit.Builder builder = new Retrofit.Builder()
//            .addConverterFactory(new NullOnEmptyConverterFactory())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .baseUrl(apiBaseUrl)
//            .client(httpClient);
//
//    public static void changeApiBaseUrl(String newBASE_URL) {
//        apiBaseUrl = newBASE_URL;
//        builder = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(new NullOnEmptyConverterFactory())
//                .baseUrl(apiBaseUrl)
//                .client(httpClient);
//
//    }
//
//    public static Retrofit.Builder getClient() {
//        if (builder == null) {
//            Gson gson = new GsonBuilder().setLenient().create();
//
//            builder = new Retrofit.Builder()
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(new NullOnEmptyConverterFactory())
//                    .baseUrl(apiBaseUrl)
//                    .client(httpClient);
//        }
//        return builder;
//    }
//
//    public static void changeDownloadBaseUrl(String newBASE_URL,
//                                             DownloadOkHtttpClient.ProgressListener progressListener) {
//        OkHttpClient downloadHttpClient = DownloadOkHtttpClient.getDownloadOkHttpClient(progressListener);
//        apiBaseUrl = newBASE_URL;
//        builder = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(new NullOnEmptyConverterFactory())
//                .baseUrl(apiBaseUrl)
//                .client(downloadHttpClient);
//    }
//
//    public static <S> S createService(Class<S> serviceClass) {
//        return builder.build().create(serviceClass);
//    }
//
//
//}
