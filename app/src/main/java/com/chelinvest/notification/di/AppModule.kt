package com.chelinvest.notification.di

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
}