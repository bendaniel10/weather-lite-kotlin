package com.bendaniel10.weatherlite.webservice

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by bendaniel on 22/06/2017.
 */


data class WeatherResponse(@SerializedName("list") val list: Array<WeatherReport>,
                           @SerializedName("city") val city: City) : Serializable

data class WeatherReport(@SerializedName("main") val main: Main,
                         @SerializedName("weather") val weather: Weather,
                         @SerializedName("clouds") val clouds: Clouds,
                         @SerializedName("dt") val dt: Long) : Serializable

data class Weather(@SerializedName("main") val main: String,
                   @SerializedName("description") val description: String,
                   @SerializedName("icon") val icon: String) : Serializable

data class Main(@SerializedName("temp") val temp: Double,
                @SerializedName("temp_min") val tempMin: Double,
                @SerializedName("temp_max") val tempMax: Double) : Serializable

data class Clouds(@SerializedName("all") val all: Double) : Serializable

data class City(@SerializedName("name") val name: String,
                @SerializedName("country") val country: String)