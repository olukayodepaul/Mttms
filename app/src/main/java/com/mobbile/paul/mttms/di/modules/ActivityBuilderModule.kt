package com.mobbile.paul.mttms.di.modules


import com.mobbile.paul.mttms.di.fragmentbuilders.FragmentBuilder
import com.mobbile.paul.mttms.di.fragmentbuilders.FragmentBuilderScope
import com.mobbile.paul.mttms.di.subcomponent.auth.AuthModule
import com.mobbile.paul.mttms.di.subcomponent.auth.AuthScope
import com.mobbile.paul.mttms.di.subcomponent.customers.CustomersModule
import com.mobbile.paul.mttms.di.subcomponent.customers.CustomersScope
import com.mobbile.paul.mttms.di.subcomponent.entries.EntriesModule
import com.mobbile.paul.mttms.di.subcomponent.entries.EntriesScope
import com.mobbile.paul.mttms.di.subcomponent.fragments.CustomerListFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.fragments.CustomerRouteFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.fragments.EntriesFragmentModule
import com.mobbile.paul.mttms.di.subcomponent.mapoutlet.MapOutletModule
import com.mobbile.paul.mttms.di.subcomponent.mapoutlet.MapOutletScope
import com.mobbile.paul.mttms.di.subcomponent.modules.ModulesModule
import com.mobbile.paul.mttms.di.subcomponent.modules.ModulesScope
import com.mobbile.paul.mttms.di.subcomponent.outlets.OutletsModule
import com.mobbile.paul.mttms.di.subcomponent.outlets.OutletsScope
import com.mobbile.paul.mttms.di.subcomponent.outletupdate.OutletsUpdateModule
import com.mobbile.paul.mttms.di.subcomponent.replist.ReplistModule
import com.mobbile.paul.mttms.di.subcomponent.replist.ReplistScope
import com.mobbile.paul.mttms.ui.auth.Userauth
import com.mobbile.paul.mttms.ui.customerlist.CustomerListViwePager
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.ui.modules.Modules
import com.mobbile.paul.mttms.ui.outlets.Outlets
import com.mobbile.paul.mttms.ui.outlets.entries.Entries
import com.mobbile.paul.mttms.ui.outlets.mapoutlet.MapOutlet
import com.mobbile.paul.mttms.ui.outlets.updateoutlets.OutletUpdate
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

    @CustomersScope
    @ContributesAndroidInjector(
        modules = [
            CustomersModule::class
        ]
    )
    abstract fun contributeCustomersModuleAndroidInjector(): Customers

    @OutletsScope
    @ContributesAndroidInjector(
        modules = [
            OutletsModule::class
        ]
    )
    abstract fun contributeOutletsModuleAndroidInjector(): Outlets

    @OutletsScope
    @ContributesAndroidInjector(
        modules = [
            OutletsUpdateModule::class
        ]
    )
    abstract fun contributeOutletsUpdateModuleAndroidInjector(): OutletUpdate


    @MapOutletScope
    @ContributesAndroidInjector(
        modules = [
            MapOutletModule::class
        ]
    )
    abstract fun contributeMapOutletModuleAndroidInjector(): MapOutlet

    @EntriesScope
    @ContributesAndroidInjector(
        modules = [
            EntriesModule::class
        ]
    )
    abstract fun contributeEntriesModuleAndroidInjector(): Entries




}