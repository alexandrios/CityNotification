package com.chelinvest.notification.di

import android.app.Application
import com.chelinvest.notification.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component (
    modules = [
        AndroidSupportInjectionModule::class
    ]
)

@Singleton
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}