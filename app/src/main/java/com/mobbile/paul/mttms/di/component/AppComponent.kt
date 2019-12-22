package com.mobbile.paul.mttms.di.component

import android.app.Application
import com.mobbile.paul.mttms.BaseApplication
import com.mobbile.paul.mttms.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
    ViewModelFactoryModule::class,
    LocalDatabaseModule::class,
    AppApi::class,
    NetworkModule::class,
    NodeJsNetworkModule::class
])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
