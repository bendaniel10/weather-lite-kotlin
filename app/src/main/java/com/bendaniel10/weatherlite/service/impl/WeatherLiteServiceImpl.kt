package com.bendaniel10.weatherlite.service.impl

import android.support.annotation.WorkerThread
import android.util.Log
import com.bendaniel10.weatherlite.service.WeatherLiteService
import com.bendaniel10.weatherlite.webservice.RestAPI
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import javax.inject.Inject

class WeatherLiteServiceImpl @Inject constructor(val restAPI: RestAPI) : WeatherLiteService {

    private val TAG = "WeatherLiteServiceImpl"

    @WorkerThread
    override fun getWeatherForcast(latitude: Double, longitude: Double): WeatherResponse? {

        var forecastForFiveDays: WeatherResponse? = null

        try {

            forecastForFiveDays = restAPI.getForecastForFiveDays(latitude, longitude).execute()?.body()

        } catch (ex: Exception) {
            Log.e(TAG, "Exception occurred", ex)
        }

        return forecastForFiveDays
    }
}