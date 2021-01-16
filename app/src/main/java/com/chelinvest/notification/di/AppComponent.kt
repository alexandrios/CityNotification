package com.chelinvest.notification.di

import android.app.Application
import com.chelinvest.notification.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component (
    modules = [
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        AppModule::class,
                okhttp3.OkHttpClient::class
    ]
)

interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}