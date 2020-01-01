package com.mobbile.paul.mttms.di.subcomponent.details

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.outlets.details.DetailsViewModel


import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class DetailsModule {
    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsModule(viewModel: DetailsViewModel): ViewModel
}