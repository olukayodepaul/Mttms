package com.mobbile.paul.mttms.di.modules



import com.mobbile.paul.mttms.providers.Api
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppApi {
    @Singleton
    @Provides
    internal fun provideMainApi(@Named("application_api") retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}