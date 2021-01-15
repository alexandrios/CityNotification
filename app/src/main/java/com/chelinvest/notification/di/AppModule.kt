package com.chelinvest.notification.di

import android.app.Application
import com.chelinvest.notification.utils.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {
//    @Provides
//    @Singleton
//    fun getUserName(): String {
//        val retval = "Vasja"
//        return retval
//    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            //            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun provideInAppMessage(): FirebaseInAppMessaging = FirebaseInAppMessaging.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)
}