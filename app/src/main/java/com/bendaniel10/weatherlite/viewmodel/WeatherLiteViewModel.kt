package com.bendaniel10.weatherlite.viewmodel

import android.databinding.ObservableField
import com.bendaniel10.weatherlite.service.WeatherLiteService
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import javax.inject.Inject

/**
 * Created by bendaniel on 22/06/2017.
 */
class WeatherLiteViewModel @Inject constructor(private val weatherLiteService: WeatherLiteService) {

    val weatherSummary: ObservableField<String> = ObservableField()
    val temperature: ObservableField<String> = ObservableField()


    fun getWeatherForecast(latitude: Double, longitude: Double): WeatherResponse? {
        return weatherLiteService.getWeatherForcast(longitude = longitude, latitude = latitude)
    }

    fun  updateUsingWeatherResponse(weatherResponse: WeatherResponse) {

        temperature.set("${weatherResponse.list.first().main.temp} º K")
        weatherSummary.set(weatherResponse.list.first().weather.first().description)

    }

}