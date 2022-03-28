package com.dekola.githublogin.di

import android.content.Context
import com.dekola.githublogin.BuildConfig
import com.dekola.githublogin.utils.Network
import com.dekola.githublogin.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class NetModule {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()

        val timeOut = 60L

        httpClient.connectTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.readTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeOut, TimeUnit.SECONDS)

        httpClient.addInterceptor(logging)

        httpClient.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            val original = builder.build()
            chain.proceed(original)
        }
        httpClient.followRedirects(true)
        httpClient.followSslRedirects(true)
        httpClient.cache(null)

        return httpClient
    }

    @Singleton
    @Provides
    fun provideApiService(builder: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

}