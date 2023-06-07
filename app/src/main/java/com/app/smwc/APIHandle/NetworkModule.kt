package com.app.omcsalesapp.APIHandle

import androidx.viewbinding.BuildConfig
import com.app.smwc.APIHandle.Apis
import com.app.ssn.data.api.ApiServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    @Named(Apis.BASE)
    fun provideRetrofit(): ApiServices {

        try {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    // builder.addHeader("Authorization", Constant.AUTH)
                    val newRequest = builder.build()
                    chain.proceed(newRequest)
                })
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(Apis.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build().create(ApiServices::class.java)
        } catch (e: Exception) {
            throw RuntimeException(e);
        }

    }
}