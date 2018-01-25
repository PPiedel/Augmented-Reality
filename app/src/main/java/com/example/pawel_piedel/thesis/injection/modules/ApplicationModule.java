package com.example.pawel_piedel.thesis.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepositoryImpl;
import com.example.pawel_piedel.thesis.data.auth.local.AccessTokenLocalDataSource;
import com.example.pawel_piedel.thesis.data.auth.local.AccessTokenLocalDataSourceImpl;
import com.example.pawel_piedel.thesis.data.auth.remote.AccessTokenApiService;
import com.example.pawel_piedel.thesis.data.auth.remote.AccessTokenRemoteSource;
import com.example.pawel_piedel.thesis.data.auth.remote.AccessTokenRemoteSourcempl;
import com.example.pawel_piedel.thesis.data.auth.remote.AuthenticationInterceptor;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepositoryImpl;
import com.example.pawel_piedel.thesis.data.business.local.LocalDataSource;
import com.example.pawel_piedel.thesis.data.business.local.LocalSource;
import com.example.pawel_piedel.thesis.data.business.remote.BusinessApiService;
import com.example.pawel_piedel.thesis.data.business.remote.BusinessRemoteDataSource;
import com.example.pawel_piedel.thesis.data.business.remote.BusinessRemoteSource;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepositoryImpl;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@Module
public class ApplicationModule {
    public static final String API_BASE_URL = "https://api.yelp.com";

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    BusinessRepository provideBusinessRepository(BusinessRemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        return new BusinessRepositoryImpl(remoteDataSource, localDataSource);
    }

    @Provides
    @Singleton
    LocalDataSource provideLocalDataSource(SharedPreferences sharedPreferences) {
        return new LocalSource(sharedPreferences);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return mApplication.getSharedPreferences("thesis_preferences", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    BusinessRemoteDataSource provideBusinessRemoteDataSource(BusinessApiService businessApiService) {
        return new BusinessRemoteSource(businessApiService);
    }

    @Provides
    @Singleton
    BusinessApiService provideBusinessApiService(Retrofit retrofit) {
        return retrofit.create(BusinessApiService.class);
    }

    @Provides
    @Singleton
    AuthenticationInterceptor provideAuthInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(@ApplicationContext Context context) {
        return new LocationRepositoryImpl(context);
    }

    @Provides
    @Singleton
    AccessTokenRepository provideAccessTokenRepository(AccessTokenLocalDataSource localSource, AccessTokenRemoteSource remoteSource) {
        return new AccessTokenRepositoryImpl(localSource, remoteSource);
    }

    @Provides
    @Singleton
    AccessTokenLocalDataSource provideAuthLocalDataSource(SharedPreferences sharedPreferences, AuthenticationInterceptor authenticationInterceptor) {
        return new AccessTokenLocalDataSourceImpl(sharedPreferences, authenticationInterceptor);
    }

    @Provides
    @Singleton
    AccessTokenRemoteSource provideAccessTokenRemoteSource(AccessTokenApiService accessTokenApiService) {
        return new AccessTokenRemoteSourcempl(accessTokenApiService);
    }

    @Provides
    @Singleton
    AccessTokenApiService provideAccessTokenApiService(Retrofit retrofit) {
        return retrofit.create(AccessTokenApiService.class);
    }

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient provideOkhttpClient(HttpLoggingInterceptor loggingInterceptor, AuthenticationInterceptor authenticationInterceptor, Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(loggingInterceptor);
        client.addInterceptor(authenticationInterceptor);
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

}
