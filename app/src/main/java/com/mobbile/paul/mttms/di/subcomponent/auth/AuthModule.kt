package com.mobbile.paul.mttms.di.subcomponent.auth

import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.di.modules.ViewModelKey
import com.mobbile.paul.mttms.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class AuthModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel
}