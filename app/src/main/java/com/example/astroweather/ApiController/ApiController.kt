package com.example.astroweather.ApiController

import retrofit2.Retrofit
import retrofit2.create
import WeatherData
import retrofit2.converter.gson.GsonConverterFactory

class ApiController  {

    var retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var whetherService= retrofit.create<WhetherService>();



    fun getWeatherData (name: String): WeatherData? {
        var call=whetherService.groupList(name,ApiConfig.key).execute()
        return call.body()
    }

}