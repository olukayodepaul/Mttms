package com.mobbile.paul.mttms.di.modules

import androidx.lifecycle.ViewModelProvider
import com.mobbile.paul.mttms.viewmodel.ViewModeProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactoryModule: ViewModeProviderFactory): ViewModelProvider.Factory
}