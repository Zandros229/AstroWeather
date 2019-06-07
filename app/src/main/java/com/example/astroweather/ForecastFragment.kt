package com.example.astroweather


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astroweather.ApiController.ApiConfig
import com.example.astroweather.ApiController.WhetherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import ForeCastDate
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import forecastItem
import kotlin.collections.ArrayList


class ForecastFragment : Fragment() {


    lateinit var fragmentView: View
    lateinit var recyclerView: RecyclerView
    lateinit var myItemRecyclerViewAdapter: MyItemRecyclerViewAdapter
    var weatherForecast: ForeCastDate? = null
    var temp=true
    lateinit var unit: String



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_forecast, container, false)

        unit=Config.units
        Thread(Runnable {
            var index = 0
            while (true) {


                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {

                            if(ForecastObject.city?.name!=Config.cityName){
                                update()
                                //weatherForecast?.city?.name=Config.cityName
                            }
                            if(unit!=Config.units){
                                update()
                                unit=Config.units
                            }
                            if(WeatherObject.name!=ForecastObject.city?.name)
                                update()
                            if(weatherForecast?.list==null)
                                update()

                            if (index >= Config.updateTimeWeather) {
                                update()
                                index = 0
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


    private fun update() {
        if (isOnline()) {
            updateFromInternet()


        } else {
            updateFronSharedPreferences()
        }
    }

    fun updateFromInternet() {
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var whetherService = retrofit.create(WhetherService::class.java)

        var call =
            whetherService.getForeCast(Config.cityName, ApiConfig.key, 40)
                .enqueue(object : Callback<ForeCastDate> {
                    override fun onFailure(call: Call<ForeCastDate>, t: Throwable) {
                        println("cos nie dziala")
                        println(t.message)
                    }

                    override fun onResponse(call: Call<ForeCastDate>, response: Response<ForeCastDate>) {
                        if (response.code() == 200) {
                            weatherForecast = response.body()
                            weatherForecast?.list = setUpGoodData(weatherForecast?.list!!)
                            if(Config.units=="C")
                                weatherForecast?.list=toCelsiusList(weatherForecast?.list!!)
                            if(Config.units=="K")
                                weatherForecast?.list=toKelvinList(weatherForecast?.list!!)
                            if(temp){
                                initRecyclerView()
                                temp=false
                            }
                            setAdapter(weatherForecast?.list!!)
                            setUpObject(weatherForecast!!)
                            updateRecyclerView()
                            if(temp){
                                initRecyclerView()
                                temp=false
                            }

                        }else if(response.code()==404){
                            Config.cityName="London"
                            Toast.makeText(fragmentView.context,"Wrong City Name ",Toast.LENGTH_LONG).show()
                        }else {
                            println(response.code().toString()+"Recylce Fragment")
                            updateRecyclerView()
                        }
                    }

                })
    }

    fun updateFronSharedPreferences() {
        if(Config.units=="C")
            ForecastObject.list=toCelsiusList(ForecastObject.list!!)
        if(Config.units=="K")
            ForecastObject.list=toKelvinList(ForecastObject.list!!)
        weatherForecast?.list=ForecastObject.list!!
        if(temp){
            initRecyclerView()
            temp=false
        }
        setAdapter(ForecastObject?.list!!)
        weatherForecast?.city=ForecastObject.city!!
        weatherForecast?.cnt=ForecastObject.cnt!!
        //weatherForecast?.message=ForecastObject.message!!
        weatherForecast?.cod=ForecastObject.cod!!
    }
    fun setUpGoodData(list: List<forecastItem>): List<forecastItem>{
        var templist :List<forecastItem> = ArrayList<forecastItem>()
        var i=8
        while (i<list.size){
            templist+=list[i]
            i+=8
        }
        templist+=list[list.size-1]
        return templist

    }

    fun setUpObject(foreCastDate: ForeCastDate) {
        ForecastObject.city=foreCastDate.city
        ForecastObject.cnt=foreCastDate.cnt
        ForecastObject.cod=foreCastDate.cod
        ForecastObject.message=ForecastObject.message
        ForecastObject.list=foreCastDate.list
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

    fun initRecyclerView() {

        if(ForecastObject.list==null)
            myItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(weatherForecast?.list!!)
        else
            myItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(ForecastObject?.list!!)
        recyclerView = fragmentView.findViewById<RecyclerView>(R.id.includedList).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = myItemRecyclerViewAdapter
        }
    }

    fun setAdapter(results: List<forecastItem>) {
        myItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(results)
        recyclerView.setAdapter(myItemRecyclerViewAdapter)
        myItemRecyclerViewAdapter.notifyDataSetChanged()
    }
    fun updateRecyclerView(){
        var i = 0
        while(i<weatherForecast?.list!!.size){
            myItemRecyclerViewAdapter.list[i].main.temp = weatherForecast!!.list[i].main.temp
            myItemRecyclerViewAdapter.list[i].main.pressure = weatherForecast!!.list[i].main.pressure
            myItemRecyclerViewAdapter.list[i].dt_txt = weatherForecast!!.list[i].dt_txt
            myItemRecyclerViewAdapter.list[i].wind.speed = weatherForecast!!.list[i].wind.speed
            myItemRecyclerViewAdapter.list[i].main.humidity = weatherForecast!!.list[i].main.humidity
            i++
        }
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
    fun toCelsiusList(list: List<forecastItem>): List<forecastItem>{
        var temp=list
        var i=0
        while (i<list.size){
            temp[i].main.temp=toCelsius(temp[i].main.temp)
            i++
        }

        return temp

    }
    fun toKelvinList(list: List<forecastItem>): List<forecastItem>{

        var i=0
        while (i<list.size){
            list[i].main.temp=toKelvin(list[i].main.temp)
            i++
        }

        return list

    }

}








