package com.bendaniel10.weatherlite

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bendaniel10.weatherlite.databinding.ActivityWeatherLiteBinding
import com.bendaniel10.weatherlite.interaction.WeatherLiteInteraction

class WeatherLiteActivity : AppCompatActivity(), WeatherLiteInteraction {

    private var binding: ActivityWeatherLiteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView<ActivityWeatherLiteBinding>(this, R.layout.activity_weather_lite)


    }
}
