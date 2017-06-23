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
class RestAPI @Inject constructor(val converterFactory: Converter.Factory,
                                  @Named("baseUrl") val baseUrl: String,
                                  @Named("weatherApiKey") val weatherApiKey: String,
                                  val okHttpClient: OkHttpClient) {

    private var openWeatherAPI: OpenWeatherMapAPI?

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build()

        openWeatherAPI = retrofit.create(OpenWeatherMapAPI::class.java)
    }

    fun getForecastForFiveDays(latitude: Long, longitude: Long): Call<WeatherResponse>? {

        return openWeatherAPI?.getForecastForFiveDays(appId = weatherApiKey, latitude = latitude, longitude = longitude)

    }
}