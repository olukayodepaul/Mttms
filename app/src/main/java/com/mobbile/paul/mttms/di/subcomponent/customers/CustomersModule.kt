package com.mobbile.paul.mttms.di.subcomponent.customers

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.customers.CustomersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class CustomersModule {
    @Binds
    @IntoMap
    @ViewModelKey(CustomersViewModel::class)
    abstract fun bindCustomersViewModel(viewModel: CustomersViewModel): ViewModel
}