package com.bendaniel10.weatherlite.service

import com.bendaniel10.weatherlite.webservice.WeatherResponse

/**
 * Created by bendaniel on 23/06/2017.
 */
interface WeatherLiteService {

    fun getWeatherForcast(latitude: Double, longitude: Double) : WeatherResponse?
}