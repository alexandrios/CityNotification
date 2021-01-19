package com.chelinvest.notification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.ui.branch.BranchViewModel
import com.chelinvest.notification.ui.limit.LimitViewModel
import com.chelinvest.notification.ui.login.LoginViewModel
import com.chelinvest.notification.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BranchViewModel::class)
    abstract fun bindBranchViewModel(branchViewModel: BranchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LimitViewModel::class)
    abstract fun bindLimitViewModel(limitViewModel: LimitViewModel): ViewModel

}