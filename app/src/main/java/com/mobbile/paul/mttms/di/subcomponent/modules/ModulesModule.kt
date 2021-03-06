package com.mobbile.paul.mttms.di.subcomponent.modules

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.modules.ModulesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class ModulesModule {
    @Binds
    @IntoMap
    @ViewModelKey(ModulesViewModel::class)
    abstract fun bindAuthViewModel(viewModel: ModulesViewModel): ViewModel
}