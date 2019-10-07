package com.mobbile.paul.mttms.di.subcomponent.outletupdate

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.modules.ModulesViewModel
import com.mobbile.paul.mttms.ui.outlets.OutletViewmodel
import com.mobbile.paul.mttms.ui.outlets.updateoutlets.OutletUpdateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class OutletsUpdateModule {
    @Binds
    @IntoMap
    @ViewModelKey(OutletUpdateViewModel::class)
    abstract fun bindOutletViewmodel(viewModel: OutletUpdateViewModel): ViewModel
}