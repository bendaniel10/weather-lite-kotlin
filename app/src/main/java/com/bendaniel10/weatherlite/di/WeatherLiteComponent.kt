package com.bendaniel10.weatherlite.di

import com.bendaniel10.weatherlite.WeatherLiteActivity
import dagger.Component

/**
 * Created by bendaniel on 22/06/2017.
 */
@Component(modules = arrayOf(WeatherLiteModule::class))
interface WeatherLiteComponent {

    fun inject(weatherLiteActivity: WeatherLiteActivity)


}