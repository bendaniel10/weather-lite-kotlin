package com.bendaniel10.weatherlite

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bendaniel10.weatherlite.databinding.ActivityWeatherLiteBinding
import com.bendaniel10.weatherlite.di.DaggerWeatherLiteComponent
import com.bendaniel10.weatherlite.di.WeatherLiteComponent
import com.bendaniel10.weatherlite.interaction.WeatherLiteInteraction
import com.bendaniel10.weatherlite.viewmodel.WeatherLiteViewModel
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherLiteActivity : AppCompatActivity(), WeatherLiteInteraction {

    private val compositeDisposable = CompositeDisposable()

    @Inject @JvmField var viewModel: WeatherLiteViewModel? = null
    private lateinit var binding: ActivityWeatherLiteBinding
    private lateinit var daggerWeatherLiteComponent: WeatherLiteComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView<ActivityWeatherLiteBinding>(this, R.layout.activity_weather_lite)

        daggerWeatherLiteComponent = DaggerWeatherLiteComponent.create()
        daggerWeatherLiteComponent.inject(this)

        binding.viewModel = viewModel
        binding.interaction = this

        refreshWeatherInformation()
    }

    private fun refreshWeatherInformation() {

        val progressDialog = ProgressDialog.show(this, getString(R.string.loading_weather_title), getString(R.string.loading_weather_desc))

        val weatherForcastSubscription = Single.fromCallable { viewModel?.getWeatherForecast(51.5033640, 0.1276250) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressDialog.dismiss() }
                .subscribe { weatherResponse, exception -> processWeatherResponse(weatherResponse, exception) }

        compositeDisposable.add(weatherForcastSubscription)
    }

    private fun processWeatherResponse(weatherResponse: WeatherResponse?, exception: Throwable?) {

        weatherResponse?.let {
            viewModel!!.updateUsingWeatherResponse(weatherResponse)

        } ?:

            Toast.makeText(this, getText(R.string.an_error_occured_please_try_again_later), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "blash", Toast.LENGTH_LONG).show()



    }

    override fun onDestroy() {
        super.onDestroy()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
