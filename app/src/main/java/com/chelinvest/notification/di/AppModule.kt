package com.chelinvest.notification.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chelinvest.notification.data.remote.RemoteService
import com.chelinvest.notification.utils.Constants.BASE_URL
import com.chelinvest.notification.utils.Constants.COMMON_PREFERENCES
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideHttpClient(app: Application): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(TikXmlConverterFactory.create())
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    fun provideRemoteService(r: Retrofit) = r.create(RemoteService::class.java)

    @Provides
    fun provideCommonPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(COMMON_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideInAppMessage(): FirebaseInAppMessaging = FirebaseInAppMessaging.getInstance()

//    @Provides
//    @Singleton
//    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)
}