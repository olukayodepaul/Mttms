package com.mobbile.paul.mttms.di.subcomponent.fragments

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.customerlist.FragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CustomerListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(FragmentViewModel::class)
    abstract fun bindCustomerListFragmentModule(viewModel: FragmentViewModel): ViewModel

}