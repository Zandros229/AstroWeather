package com.example.astroweather


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import WeatherData
import com.example.astroweather.ApiController.ApiController

class SimpleData : Fragment() {


    lateinit var fragmentView: View
    lateinit var name: TextView
    lateinit var temp: TextView
    lateinit var pressure: TextView
    lateinit var longitude: TextView
    lateinit var latitude: TextView
    lateinit var apiController: ApiController
    var weatherData: WeatherData? = null


    fun initTextViews() {
        name = fragmentView.findViewById(R.id.namesimple)
        temp = fragmentView.findViewById(R.id.tempsimple)
        pressure = fragmentView.findViewById(R.id.pressuresimple)
        longitude = fragmentView.findViewById(R.id.longitudesimple)
        latitude = fragmentView.findViewById(R.id.latitudesimple)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_data, container, false)

        initTextViews()

        weatherData = apiController.getWeatherData("London")

        name.text = weatherData?.name
        temp.text = weatherData?.main?.temp.toString()
        pressure.text = weatherData?.main?.pressure.toString()
        longitude.text = weatherData?.coord?.lon.toString()
        latitude.text = weatherData?.coord?.lat.toString()

    }


}
