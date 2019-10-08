package com.mobbile.paul.mttms.di.subcomponent.entries


import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.outlets.entries.EntriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class EntriesModule {
    @Binds
    @IntoMap
    @ViewModelKey(EntriesViewModel::class)
    abstract fun bindEntriesViewModel(viewModel: EntriesViewModel): ViewModel
}