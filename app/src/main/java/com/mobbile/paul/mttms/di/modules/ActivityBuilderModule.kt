package com.mobbile.paul.mttms.di.modules


import com.mobbile.paul.mttms.di.fragmentbuilders.FragmentBuilder
import com.mobbile.paul.mttms.di.fragmentbuilders.FragmentBuilderScope
import com.mobbile.paul.mttms.di.subcomponent.auth.AuthModule
import com.mobbile.paul.mttms.di.subcomponent.auth.AuthScope
import com.mobbile.paul.mttms.di.subcomponent.fragments.CustomerListFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.fragments.CustomerRouteFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.fragments.EntriesFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.modules.ModulesModule
import com.mobbile.paul.mttms.di.subcomponent.modules.ModulesScope
import com.mobbile.paul.mttms.di.subcomponent.replist.ReplistModule
import com.mobbile.paul.mttms.di.subcomponent.replist.ReplistScope
import com.mobbile.paul.mttms.ui.auth.Userauth
import com.mobbile.paul.mttms.ui.customerlist.CustomerListViwePager
import com.mobbile.paul.mttms.ui.modules.Modules
import com.mobbile.paul.mttms.ui.replist.RepList
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthModule::class
        ]
    )
    abstract fun contributeActivityAndroidInjector(): Userauth

    @ModulesScope
    @ContributesAndroidInjector(
        modules = [
            ModulesModule::class
        ]
    )
    abstract fun contributeModulesModuleAndroidInjector(): Modules

    @ReplistScope
    @ContributesAndroidInjector(
        modules = [
            ReplistModule::class
        ]
    )
    abstract fun contributeReplistModuleAndroidInjector(): RepList

    @FragmentBuilderScope
    @ContributesAndroidInjector(modules = [
        FragmentBuilder::class,
        CustomerListFragmentModule::class,
        CustomerRouteFragmentModule::class,
        EntriesFragmentModule::class
    ])
    abstract fun contributeSalesPagerActivity(): CustomerListViwePager
}