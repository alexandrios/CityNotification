package com.chelinvest.notification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.ui.fragments.address.AddressViewModel
import com.chelinvest.notification.ui.fragments.address.edit.EditAddressViewModel
import com.chelinvest.notification.ui.fragments.branch.BranchViewModel
import com.chelinvest.notification.ui.fragments.limit.LimitViewModel
import com.chelinvest.notification.ui.fragments.login.LoginViewModel
import com.chelinvest.notification.ui.fragments.page.PageViewModel
import com.chelinvest.notification.ui.fragments.subscr.SubscrViewModel
import com.chelinvest.notification.ui.fragments.subscr.edit.EditSubscrViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(SubscrViewModel::class)
    abstract fun bindSubscrViewModel(subscrViewModel: SubscrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditSubscrViewModel::class)
    abstract fun bindEditSubscrViewModel(editSubscrViewModel: EditSubscrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel::class)
    abstract fun bindAddressViewModel(addressViewModel: AddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAddressViewModel::class)
    abstract fun bindeditAddressViewModel(editAddressViewModel: EditAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel::class)
    abstract fun bindPageViewModel(pageViewModel: PageViewModel): ViewModel
}