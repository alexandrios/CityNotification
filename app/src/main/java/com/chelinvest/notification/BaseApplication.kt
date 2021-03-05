package com.chelinvest.notification

import com.chelinvest.notification.di.DaggerAppComponent
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()

        // Инициализация YandexMetrica
        val config = YandexMetricaConfig.newConfigBuilder("36360897-a0ec-476c-9e08-5e5bf4cb24b2").build() // Creating an extended library configuration
        YandexMetrica.activate(applicationContext, config) // Initializing the AppMetrica SDK
        YandexMetrica.enableActivityAutoTracking(this) // Automatic tracking of user activity
    }
}