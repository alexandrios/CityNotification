package com.chelinvest.notification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.ui.branch.BranchViewModel
import com.chelinvest.notification.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindMainViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BranchViewModel::class)
    abstract fun bindBranchViewModel(branchViewModel: BranchViewModel): ViewModel

/*
        @Binds
        @IntoMap
        @ViewModelKey(FavouritesViewModel::class)
        abstract fun bindFavouritesViewModel(favouritesViewModel: FavouritesViewModel) : ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(SettingsViewModel::class)
        abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel) : ViewModel
    */

}