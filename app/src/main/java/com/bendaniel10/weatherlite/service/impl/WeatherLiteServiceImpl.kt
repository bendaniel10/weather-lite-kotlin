package com.bendaniel10.weatherlite.service.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.WorkerThread
import android.util.Log
import com.bendaniel10.weatherlite.app.WeatherLiteApplication
import com.bendaniel10.weatherlite.service.WeatherLiteService
import com.bendaniel10.weatherlite.webservice.RestAPI
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import java.net.URL
import javax.inject.Inject

class WeatherLiteServiceImpl @Inject constructor(val restAPI: RestAPI) : WeatherLiteService {

    private val TAG = "WeatherLiteServiceImpl"

    override fun getWeatherConditionBitmapForIcon(icon: String?): Bitmap? {

        var bitmap: Bitmap? = null

        try {

            val iconUrl = URL(WeatherLiteApplication.OPEN_WEATHER_API_IMAGE_URL + icon + ".png")
            bitmap = BitmapFactory.decodeStream(iconUrl.openConnection().getInputStream())

        } catch (ex: Exception) {

            Log.e(TAG, "Exception occurred", ex)

        }

        return bitmap
    }


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