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
import forecastItem
import kotlin.collections.ArrayList


class ForecastFragment : Fragment() {

    lateinit var fragmentView: View
    lateinit var recyclerView: RecyclerView
    lateinit var myItemRecyclerViewAdapter: MyItemRecyclerViewAdapter
    var weatherForecast: ForeCastDate? = null
    var temp=true;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_forecast, container, false)


        Thread(Runnable {
            var index = 0
            while (true) {


                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {

                            if (index >= Config.updateTime) {
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
                            if(temp){
                                initRecyclerView()
                                temp=false
                            }
                            setUpObject(weatherForecast!!)
                            updateRecyclerView()
                            if(temp){
                                initRecyclerView()
                                temp=false
                            }

                        } else {
                            println(response.code())
                        }
                    }

                })
    }

    fun updateFronSharedPreferences() {
        weatherForecast?.list=ForecastObject.list!!
        weatherForecast?.city=ForecastObject.city!!
        weatherForecast?.cnt=ForecastObject.cnt!!
        weatherForecast?.message=ForecastObject.message!!
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


        myItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(weatherForecast?.list!!)
        recyclerView = fragmentView.findViewById<RecyclerView>(R.id.includedList).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = myItemRecyclerViewAdapter
        }
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
}








