package com.example.astroweather


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import WeatherData
import android.content.Context
import android.os.StrictMode
import android.provider.SyncStateContract.Helpers.update
import android.widget.Toast
import com.example.astroweather.ApiController.ApiConfig
import com.example.astroweather.ApiController.ApiController
import com.example.astroweather.ApiController.WhetherService
import kotlinx.android.synthetic.main.fragment_moon.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.CONNECTIVITY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_simple_data.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class SimpleData : Fragment() {


    lateinit var fragmentView: View
    lateinit var name: TextView
    lateinit var temp: TextView
    lateinit var pressure: TextView
    lateinit var longitude: TextView
    lateinit var latitude: TextView
    lateinit var time: TextView
    var weatherData: WeatherData? = null
    lateinit var unit:String
    lateinit var pic: ImageView


    fun initTextViews() {
        name = fragmentView.findViewById(R.id.namesimple)
        temp = fragmentView.findViewById(R.id.tempsimple)
        pressure = fragmentView.findViewById(R.id.pressuresimple)
        longitude = fragmentView.findViewById(R.id.longitudesimple)
        latitude = fragmentView.findViewById(R.id.latitudesimple)
        time = fragmentView.findViewById(R.id.actualTimeSimple)
        pic = fragmentView.findViewById(R.id.imageView)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_simple_data, container, false)
        unit=Config.units

        initTextViews()
        val sdf = SimpleDateFormat("HH:mm:ss")
        Thread(Runnable {
            var index = 0
            while (true) {

                var currentDate = sdf.format(Date())
                currentDate = sdf.format(Date())

                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {

                            if(WeatherObject.name!=Config.cityName) {
                                update()
                                WeatherObject.name=Config.cityName
                            }
                            if(name.text!=Config.cityName) {
                                update()
                            }
                            if(unit!=Config.units){
                                update()
                                unit=Config.units
                            }
                            if (index >= Config.updateTimeWeather) {
                                update()
                                index = 0
                            }
                            if(weatherData==null)
                                update()
                            try {
                                time.text = currentDate.toString()
                            } catch (e: Exception) {

                            }
                        }
                    }
                } catch (e: Exception) {
                    if (activity != null) {
                        activity!!.finish()
                    }
                }


                Thread.sleep(1000)
                index++
            }

        }).start()


        return fragmentView

    }

    fun update() {
        if (isOnline()) {
            updateFromInternet()
            Toast.makeText(this.context, "Updated form Internet", Toast.LENGTH_LONG).show()
        } else {
            updateFronSharedPreferences()
            Toast.makeText(this.context, "Updated form Shared Preferences", Toast.LENGTH_LONG).show()
        }
    }

    fun updateFronSharedPreferences() {

        if(Config.units=="C")
            temp.text=toCelsius(WeatherObject.main?.temp!!).toString()
        if(Config.units=="K")
            temp.text=toKelvin(WeatherObject.main?.temp!!).toString()
        name.text = WeatherObject.name
        pressure.text = WeatherObject.main?.pressure.toString()
        longitude.text = WeatherObject.coord?.lon.toString()
        latitude.text = WeatherObject.coord?.lat.toString()
        Config.latitudeSafe = WeatherObject.coord?.lat!!
        Config.longitudeSafe = WeatherObject.coord?.lon!!
    }

    fun updateFromInternet() {
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var whetherService = retrofit.create(WhetherService::class.java)

        var call =
            whetherService.groupList(Config.cityName, ApiConfig.key).enqueue(object : Callback<WeatherData> {
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    println("cos nie dziala")
                    println(t.message)
                }

                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.code() == 200) {
                        weatherData = response.body()
                        if(Config.units=="C")
                            weatherData?.main?.temp=toCelsius(weatherData?.main?.temp!!)
                        if(Config.units=="K")
                            weatherData?.main?.temp=toKelvin(weatherData?.main?.temp!!)
                        setUpObject(weatherData!!)
                        name.text = weatherData?.name
                        temp.text = weatherData?.main?.temp.toString()
                        pressure.text = weatherData?.main?.pressure.toString()
                        longitude.text = weatherData?.coord?.lon.toString()
                        latitude.text = weatherData?.coord?.lat.toString()
                        Picasso.with(context).load("https://openweathermap.org/img/w/"+weatherData?.weather?.get(0)?.icon+".png").into(pic)
                        Config.latitudeSafe = weatherData?.coord?.lat!!
                        Config.longitudeSafe = weatherData?.coord?.lon!!
                    } else {
                        println(response.code().toString()+ " Simple Fragment")
                    }
                }

            })
    }

    private fun setUpObject(weatherobject: WeatherData) {
        WeatherObject.base = weatherobject.base
        WeatherObject.clouds = weatherobject.clouds
        WeatherObject.cod = weatherobject.cod
        WeatherObject.coord = weatherobject.coord
        WeatherObject.dt = weatherobject.dt
        WeatherObject.id = weatherobject.id
        WeatherObject.main = weatherobject.main
        WeatherObject.name = weatherobject.name
        WeatherObject.sys = weatherobject.sys
        WeatherObject.timezone = weatherobject.timezone
        WeatherObject.visibility = weatherobject.visibility
        WeatherObject.weather = weatherobject.weather
        WeatherObject.wind = weatherobject.wind
    }

    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }


        var result = 2
        if (result > 0) {
            result = 100
        } else {
            result = 0
        }
        return false
    }
    fun toCelsius(kelvin: Double):Double{
        if(kelvin>150&&kelvin<400)
            return kelvin-273.15
        else
            return kelvin
    }
    fun toKelvin(celsius: Double): Double{
        if(celsius<100&&celsius>-100)
            return celsius+273.15
        else
            return  celsius
    }


}
