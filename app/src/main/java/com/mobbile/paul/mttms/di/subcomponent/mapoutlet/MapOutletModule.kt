package com.mobbile.paul.mttms.di.subcomponent.mapoutlet

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.outlets.mapoutlet.MapOutletViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class MapOutletModule {
    @Binds
    @IntoMap
    @ViewModelKey(MapOutletViewModel::class)
    abstract fun bindMapOutletViewModel(viewModel: MapOutletViewModel): ViewModel
}