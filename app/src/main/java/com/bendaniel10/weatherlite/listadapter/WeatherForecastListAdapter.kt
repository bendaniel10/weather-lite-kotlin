package com.bendaniel10.weatherlite.listadapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bendaniel10.weatherlite.R
import com.bendaniel10.weatherlite.databinding.WeatherForecastRowBinding
import com.bendaniel10.weatherlite.webservice.WeatherReport
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bendaniel on 27/06/2017.
 */

class WeatherForecastListAdapter(private val weatherReports: MutableList<WeatherReport>) : RecyclerView.Adapter<WeatherForecastListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder? {

        val binding = DataBindingUtil.inflate<WeatherForecastRowBinding>(
                LayoutInflater.from(parent.context),
                R.layout.weather_forecast_row,
                parent,
                false
        )

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val weatherReport = weatherReports[position]

        if (holder.binding != null) {

            holder.binding!!.dateTxt.text = SimpleDateFormat("d MMM, HH:mm aaa", Locale.getDefault()).format(Date(weatherReport.dt * 1000L))
            holder.binding!!.weatherSummaryTxt.text = weatherReport.weather.first().description

            Log.d("DATE", "${weatherReport.dt} value for date.")
        }


    }

    fun updateList(newItems: MutableList<WeatherReport>) {
        weatherReports.clear()
        weatherReports.addAll(newItems)
    }

    override fun getItemCount(): Int {

        return weatherReports.size

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: WeatherForecastRowBinding? = null

        constructor(binding: WeatherForecastRowBinding?) : this(binding!!.root) {

            this.binding = binding

        }

    }

}
