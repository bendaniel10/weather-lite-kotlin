package com.bendaniel10.weatherlite.viewmodel

import android.databinding.ObservableField
import android.graphics.Bitmap
import com.bendaniel10.weatherlite.service.WeatherLiteService
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by bendaniel on 22/06/2017.
 */
class WeatherLiteViewModel @Inject constructor(private val weatherLiteService: WeatherLiteService) {

    val weatherSummary: ObservableField<String> = ObservableField()
    val temperature: ObservableField<String> = ObservableField()
    val weatherConditionIcon: PublishSubject<String> = PublishSubject.create()


    fun getWeatherForecast(latitude: Double, longitude: Double): WeatherResponse? {
        return weatherLiteService.getWeatherForcast(longitude = longitude, latitude = latitude)
    }

    fun updateUsingWeatherResponse(weatherResponse: WeatherResponse) {

        temperature.set("${weatherResponse.list.first().main.temp.toCelsiusFormatted()} º C")
        weatherSummary.set(weatherResponse.list.first().weather.first().description)
        weatherConditionIcon.onNext(weatherResponse.list.first().weather.first().icon)
    }

    fun getWeatherConditionBitmapForIcon(icon: String?): Bitmap? {
        return weatherLiteService.getWeatherConditionBitmapForIcon(icon)
    }


    fun Double.toCelsiusFormatted() : String {
        return "%.2f".format(this - 273.15)
    }
}