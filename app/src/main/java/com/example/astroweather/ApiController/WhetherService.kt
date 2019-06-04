package com.example.astroweather.ApiController

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import WeatherData

interface WhetherService {
    //Generate your own Api key and put it in this link
    @GET("weather?q={name}&APIkey={key}")
    fun groupList(@Path("name") name: String, @Path("key") key: String): Call<WeatherData>

}