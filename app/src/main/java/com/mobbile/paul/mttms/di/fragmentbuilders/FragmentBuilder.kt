package com.mobbile.paul.mttms.di.fragmentbuilders


import com.mobbile.paul.mttms.ui.customerlist.fragments.CustomerListFragment
import com.mobbile.paul.mttms.ui.customerlist.fragments.CustomerRouteFragment
import com.mobbile.paul.mttms.ui.customerlist.fragments.EntriesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun  contributeCustomerListFragment(): CustomerListFragment

    @ContributesAndroidInjector
    abstract fun  contributeEntriesFragment(): EntriesFragment

    @ContributesAndroidInjector
    abstract fun  contributeCustomerRouteFragment(): CustomerRouteFragment

}
