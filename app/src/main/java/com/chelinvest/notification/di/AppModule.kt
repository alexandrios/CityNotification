package com.chelinvest.notification.di

import android.app.Application
import com.chelinvest.notification.BaseApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun getUserName(): String {
        val retval = "Vasja"
        return retval
    }

    @Provides
    @Singleton
    fun provideInAppMessage(): FirebaseInAppMessaging = FirebaseInAppMessaging.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)
}