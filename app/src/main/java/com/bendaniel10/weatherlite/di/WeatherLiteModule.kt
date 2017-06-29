package com.bendaniel10.weatherlite.di

import com.bendaniel10.weatherlite.app.WeatherLiteApplication
import com.bendaniel10.weatherlite.service.WeatherLiteService
import com.bendaniel10.weatherlite.service.impl.WeatherLiteServiceImpl
import com.bendaniel10.weatherlite.viewmodel.WeatherLiteViewModel
import com.bendaniel10.weatherlite.webservice.RestAPI
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideOkHttpClient(@Named("connectionTimeOutSecs") connectionTimeOutSecs: Long,
                            @Named("readTimeOutSecs") readTimeOutSecs: Long,
                            @Named("interceptor") interceptor: Interceptor?): OkHttpClient {

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(connectionTimeOutSecs, TimeUnit.SECONDS)
                .readTimeout(readTimeOutSecs, TimeUnit.SECONDS)
                .build()

        if (interceptor != null) {

            val interceptorBuilder = okHttpClient.newBuilder()

            interceptorBuilder.addInterceptor(interceptor)

            return interceptorBuilder.build()
        }

        return okHttpClient

    }

    @Provides
    @Named("interceptor")
    fun providesInterceptor(): Interceptor {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return logInterceptor
    }

    @Provides
    @Named("readTimeOutSecs")
    fun provideReadTimeOutSecs() = 15L

    @Provides
    @Named("connectionTimeOutSecs")
    fun provideConnectionTimeOutSecs() = 15L

    @Provides
    fun provideRestAPI(converterFactory: Converter.Factory,
                       @Named("baseUrl") baseUrl: String,
                       okHttpClient: OkHttpClient,
                       @Named("weatherApiKey") weatherApiKey: String): RestAPI {

        return RestAPI(converterFactory = converterFactory, baseUrl = baseUrl, okHttpClient = okHttpClient, weatherApiKey = weatherApiKey)

    }

    @Provides
    fun provideWeatherLiteService(restAPI: RestAPI): WeatherLiteService {
        return WeatherLiteServiceImpl(restAPI = restAPI)
    }

    @Provides
    fun provideWeatherLiteViewModel(weatherLiteService: WeatherLiteService): WeatherLiteViewModel {
        return WeatherLiteViewModel(weatherLiteService = weatherLiteService)
    }

}