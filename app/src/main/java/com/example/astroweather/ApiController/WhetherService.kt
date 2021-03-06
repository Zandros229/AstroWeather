package com.example.astroweather.ApiController

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import WeatherData
import retrofit2.http.Query
import ForeCastDate

interface WhetherService {
    //Generate your own Api key and put it in this link
    @GET("weather?")
    fun groupList(@Query("q") name: String, @Query("APIkey") key: String): Call<WeatherData>
    @GET("forecast?")
    fun getForeCast(@Query("q") name: String, @Query("APIkey") key: String,  @Query("cnt") cnt: Int): Call<ForeCastDate>
    

}