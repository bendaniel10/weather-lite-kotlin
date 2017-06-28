package com.bendaniel10.weatherlite

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.bendaniel10.weatherlite.databinding.ActivityWeatherLiteBinding
import com.bendaniel10.weatherlite.di.DaggerWeatherLiteComponent
import com.bendaniel10.weatherlite.di.WeatherLiteComponent
import com.bendaniel10.weatherlite.interaction.WeatherLiteInteraction
import com.bendaniel10.weatherlite.listadapter.WeatherForecastListAdapter
import com.bendaniel10.weatherlite.viewmodel.WeatherLiteViewModel
import com.bendaniel10.weatherlite.webservice.WeatherResponse
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherLiteActivity : AppCompatActivity(), WeatherLiteInteraction, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private val compositeDisposable = CompositeDisposable()
    private val TAG = "WeatherLiteActivity"
    @Inject @JvmField var viewModel: WeatherLiteViewModel? = null
    private lateinit var binding: ActivityWeatherLiteBinding
    private lateinit var daggerWeatherLiteComponent: WeatherLiteComponent
    private var lastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    companion object {
        const val MY_LOCATION_REQUEST_CODE = 10
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 11
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView<ActivityWeatherLiteBinding>(this, R.layout.activity_weather_lite)

        daggerWeatherLiteComponent = DaggerWeatherLiteComponent.create()
        daggerWeatherLiteComponent.inject(this)

        binding.viewModel = viewModel
        binding.interaction = this

        binding.dayForcastRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.dayForcastRecyclerView.adapter = WeatherForecastListAdapter(mutableListOf())

        if (checkPlayServices()) {

            buildGoogleApiClient()

        }
    }

    private fun refreshWeatherInformation() {

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        binding.rootView.isRefreshing = true

        if (lastLocation != null) {

            val weatherForecastSubscription = Single.fromCallable { viewModel?.getWeatherForecast(lastLocation!!.latitude, lastLocation!!.longitude) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { binding.rootView.isRefreshing = false }
                    .subscribe { weatherResponse, exception -> processWeatherResponse(weatherResponse, exception) }

            compositeDisposable.add(weatherForecastSubscription)

            val weatherConditionIconSubscription = viewModel?.weatherConditionIcon
                    ?.subscribe { updateWeatherConditionIconDrawable(it) }

            compositeDisposable.addAll(weatherConditionIconSubscription)

        }
    }

    private fun updateWeatherConditionIconDrawable(icon: String?) {

        Single.fromCallable { viewModel?.getWeatherConditionBitmapForIcon(icon) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap, exception -> processWeatherConditionIconDrawableResponse(bitmap, exception) }


    }

    private fun processWeatherConditionIconDrawableResponse(bitmap: Bitmap?, ex: Throwable?) {

        if (bitmap != null) {
            binding.weatherStatusImg.setImageBitmap(bitmap)
        } else {
            Log.e(TAG, "Exception occurred", ex)
        }

    }

    private fun processWeatherResponse(weatherResponse: WeatherResponse?, ex: Throwable?) {

        weatherResponse?.let {
            viewModel!!.updateUsingWeatherResponse(weatherResponse)

            val weatherForecastListAdapter = binding.dayForcastRecyclerView.adapter as WeatherForecastListAdapter
            weatherForecastListAdapter.updateList(weatherResponse.list.toMutableList())
            weatherForecastListAdapter.notifyDataSetChanged()


        } ?: notifyWeatherRefreshFailed()


    }

    private fun notifyWeatherRefreshFailed() {
        Toast.makeText(this, getText(R.string.an_error_occured_please_try_again_later), Toast.LENGTH_SHORT).show()
    }

    private fun refreshWeatherInfoIfPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            refreshWeatherInformation()

        } else {

            showRequestLocationPermissionRationale()

        }
    }

    private fun showRequestLocationPermissionRationale() {

        AlertDialog.Builder(this)
                .setTitle(getString(R.string.request_location_permisson_rationale_dialog_title))
                .setMessage(getString(R.string.request_location_permisson_rationale_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.grant_permission), { _, _ -> requestLocationPermission() })
                .show()

    }

    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(
                        this,
                        getText(R.string.location_permission_granted),
                        Toast.LENGTH_SHORT
                ).show()

                refreshWeatherInformation()

            } else {

                showRequestLocationPermissionRationale()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun checkPlayServices(): Boolean {

        val googleApiAvailability = GoogleApiAvailability.getInstance()

        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (googleApiAvailability.isUserResolvableError(resultCode)) {

                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Toast.makeText(applicationContext, getString(R.string.device_is_not_supported), Toast.LENGTH_LONG).show()
                finish()
            }

            return false
        }
        return true
    }

    @Synchronized private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    override fun onConnected(p0: Bundle?) {

        refreshWeatherInfoIfPermissionGranted()

    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(result: ConnectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.errorCode)

    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient?.disconnect()
    }

    override fun onResume() {
        super.onResume()

        checkPlayServices()
    }

    override fun onRefreshWeatherInformation() {

        refreshWeatherInfoIfPermissionGranted()

    }

}
