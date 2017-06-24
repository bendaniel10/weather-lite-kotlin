package com.bendaniel10.weatherlite.app

import android.app.Application

/**
 * Created by bendaniel on 22/06/2017.
 */
class WeatherLiteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: WeatherLiteApplication
            private set
        const val OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/"
        const val OPEN_WEATHER_API_IMAGE_URL = "http://openweathermap.org/img/w/"
        const val OPEN_WEATHER_API_KEY = "b95b4c8a2948f33d41fce8d563f3c1b4"

    }

}