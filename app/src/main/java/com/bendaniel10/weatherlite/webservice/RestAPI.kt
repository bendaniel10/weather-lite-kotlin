package com.bendaniel10.weatherlite.webservice

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by bendaniel on 23/06/2017.
 */
class RestAPI @Inject constructor(converterFactory: Converter.Factory,
                                  @Named("baseUrl") baseUrl: String,
                                  private @Named("weatherApiKey") val weatherApiKey: String,
                                  okHttpClient: OkHttpClient) {

    private var openWeatherAPI: OpenWeatherMapAPI

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build()

        openWeatherAPI = retrofit.create(OpenWeatherMapAPI::class.java)
    }

    fun getForecastForFiveDays(latitude: Double, longitude: Double): Call<WeatherResponse?> {

        return openWeatherAPI.getForecastForFiveDays(appId = weatherApiKey, latitude = latitude, longitude = longitude)

    }
}