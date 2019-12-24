package com.mobbile.paul.mttms.di.subcomponent.attendant

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.attendant_basket.AttendantViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class AttendantModule {
    @Binds
    @IntoMap
    @ViewModelKey(AttendantViewModel::class)
    abstract fun bindAttendantModule(viewModel: AttendantViewModel): ViewModel
}