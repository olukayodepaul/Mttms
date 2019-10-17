package com.mobbile.paul.mttms.di.subcomponent.sku

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.outlets.sku.SkuViewmodel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class SkuModule {
    @Binds
    @IntoMap
    @ViewModelKey(SkuViewmodel::class)
    abstract fun bindSkuViewmodel(viewModel: SkuViewmodel): ViewModel
}