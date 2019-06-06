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
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class AdditionallyData : Fragment() {


    lateinit var fragmentView: View
    lateinit var name: TextView
    lateinit var windSpeed: TextView
    lateinit var windDeg: TextView
    lateinit var visibility: TextView
    lateinit var clouds: TextView
    lateinit var humindity: TextView
    var weatherData: WeatherData? = null
    lateinit var unit:String


    fun initTextViews() {
        name = fragmentView.findViewById(R.id.nameAdd)
        windSpeed = fragmentView.findViewById(R.id.windSpeedAdd)
        windDeg = fragmentView.findViewById(R.id.windDegAdd)
        visibility = fragmentView.findViewById(R.id.visibilityAdd)
        clouds = fragmentView.findViewById(R.id.cloudsAdd)
        humindity = fragmentView.findViewById(R.id.humindityAdd)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_additionally_data, container, false)
        unit=Config.units

        initTextViews()
        Thread(Runnable {
            var index = 0
            while (true) {

                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {

                            if(WeatherObject.name!=Config.cityName) {
                                update()
                                WeatherObject.name=Config.cityName
                            }
                            if(name.text=="TextView")
                                update()
                            if(unit!=Config.units){
                                update()
                                unit=Config.units
                            }
                            if(name.text!=WeatherObject.name||name.text==null) {
                                update()
                                name.text=WeatherObject.name
                            }
                            if(weatherData==null)
                                update()


                            if (index >= Config.updateTimeWeather) {
                                update()
                                index = 0
                            }
                            try {
                            } catch (e: Exception) {

                            }
                        }
                    }
                } catch (e: Exception) {
                    if(activity != null){
                        activity!!.finish()
                    }
                }


                Thread.sleep(1000)
                index++
            }

        }).start()


        return fragmentView

    }

    fun update(){
        if(isOnline()) {
            updateFromInternet()
            Toast.makeText(this.context,"Updated form Internet",Toast.LENGTH_LONG).show()
        }
        else {
            updateFronSharedPreferences()
            Toast.makeText(this.context,"Updated form Shared Preferences",Toast.LENGTH_LONG).show()
        }
    }
    fun updateFronSharedPreferences(){

        name.text = WeatherObject.name
        windSpeed.text = WeatherObject.wind?.speed.toString()
        windDeg.text = WeatherObject.wind?.deg.toString()
        visibility.text = WeatherObject.visibility.toString()
        clouds.text = WeatherObject.clouds.toString()
        humindity.text= WeatherObject.main?.humidity.toString()
        name.text = WeatherObject.name
    }

    fun updateFromInternet(){
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
                        setUpObject(weatherData!!)
                        name.text = weatherData?.name
                        windSpeed.text = weatherData?.wind?.speed.toString()
                        windDeg.text = weatherData?.wind?.deg.toString()
                        visibility.text = weatherData?.visibility.toString()
                        clouds.text = weatherData?.clouds.toString()
                        humindity.text= weatherData?.main?.humidity.toString()
                    } else {
                        println(response.code().toString()+ "Add Fragment")
                    }
                }

            })}
    private fun setUpObject(weatherobject: WeatherData){
        WeatherObject.base=weatherobject.base
        WeatherObject.clouds=weatherobject.clouds
        WeatherObject.cod=weatherobject.cod
        WeatherObject.coord=weatherobject.coord
        WeatherObject.dt=weatherobject.dt
        WeatherObject.id=weatherobject.id
        WeatherObject.main=weatherobject.main
        WeatherObject.name=weatherobject.name
        WeatherObject.sys=weatherobject.sys
        WeatherObject.timezone=weatherobject.timezone
        WeatherObject.visibility=weatherobject.visibility
        WeatherObject.weather=weatherobject.weather
        WeatherObject.wind=weatherobject.wind
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

        return false
    }


}
