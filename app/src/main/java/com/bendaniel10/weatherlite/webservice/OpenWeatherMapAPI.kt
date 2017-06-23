package com.bendaniel10.weatherlite.webservice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by bendaniel on 22/06/2017.
 */
interface OpenWeatherMapAPI {

    @GET("forecast")
    fun getForecastForFiveDays(@Query("appid") appId: String,
                               @Query("lon") longitude: Double,
                               @Query("lat") latitude: Double) : Call<WeatherResponse?>


}