package com.mobbile.paul.mttms.di.modules


import android.app.Application
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import com.mobbile.paul.mttms.BuildConfig

@Module
@Suppress("unused")
class NetworkModule {

    @Provides
    @Singleton
    @Named("application_api")
    internal fun provideRetrofitInstance(application: Application): Retrofit {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            okHttpClientBuilder
                //.addInterceptor(ConnectivityInterceptorImpl(application))
                .addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl("http://82.163.72.135:8092")
            .client(okHttpClientBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
       }
}
