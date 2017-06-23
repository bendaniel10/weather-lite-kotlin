package com.bendaniel10.weatherlite.di

import com.bendaniel10.weatherlite.app.WeatherLiteApplication
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Created by bendaniel on 22/06/2017.
 */
@Module class WeatherLiteModule {


    @Provides
    fun provideApplicationContext(): WeatherLiteApplication {
        return WeatherLiteApplication.instance
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return WeatherLiteApplication.OPEN_WEATHER_API_URL
    }

    @Provides
    @Named("weatherApiKey")
    fun provideWeatherApiKey(): String {
        return WeatherLiteApplication.OPEN_WEATHER_API_KEY
    }

    @Provides
    fun provideOkHttpClient(connectionTimeOutSecs: Long, readTimeOutSecs: Long, interceptors: Array<Interceptor>?): OkHttpClient {

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(connectionTimeOutSecs, TimeUnit.SECONDS)
                .readTimeout(readTimeOutSecs, TimeUnit.SECONDS)
                .build()

        interceptors?.let {

            val interceptorBuilder = okHttpClient.newBuilder()

            for (interceptor in interceptors) {

                interceptorBuilder.addInterceptor(interceptor)

            }

            return okHttpClient
        }

        return okHttpClient

    }

}