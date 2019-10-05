package com.mobbile.paul.mttms


import com.jakewharton.threetenabp.AndroidThreeTen
import com.mobbile.paul.mttms.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AndroidThreeTen.init(this)
        return DaggerAppComponent.builder().application(this).build()
    }
}
