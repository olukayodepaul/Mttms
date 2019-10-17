package com.mobbile.paul.mttms.di.subcomponent.outlets

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.outlets.OutletViewmodel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class OutletsModule {
    @Binds
    @IntoMap
    @ViewModelKey(OutletViewmodel::class)
    abstract fun bindOutletViewmodel(viewModel: OutletViewmodel): ViewModel
}