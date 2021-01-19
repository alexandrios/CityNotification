package com.chelinvest.notification.di

import com.chelinvest.notification.ui.branch.BranchFragment
import com.chelinvest.notification.ui.limit.LimitFragment
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

    @ContributesAndroidInjector
    abstract fun contributeBranchFragment(): BranchFragment

    @ContributesAndroidInjector
    abstract fun contributeLimitFragment(): LimitFragment

    /*
        @ContributesAndroidInjector
        abstract fun contributeFavouritesFragment(): FavouritesFragment

        @ContributesAndroidInjector
        abstract fun contributeSettingsFragment(): SettingsFragment

        @ContributesAndroidInjector
        abstract fun contributeDbService(): DbService
    */
}