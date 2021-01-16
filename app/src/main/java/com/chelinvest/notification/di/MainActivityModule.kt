package com.chelinvest.notification.di

import com.chelinvest.notification.ui.login.LoginFragment
import com.chelinvest.notification.ui.main.MainActivity
import dagger.android.ContributesAndroidInjector
import dagger.Module

@Module(includes = [ViewModelModule::class])
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

/*

    @ContributesAndroidInjector
    abstract fun contributeHistoryFragment(): HistoryFragment

    @ContributesAndroidInjector
    abstract fun contributeFavouritesFragment(): FavouritesFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeDbService(): DbService
*/
}