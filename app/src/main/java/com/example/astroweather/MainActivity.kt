package com.example.astroweather

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.example.astroweather.WeatherObject as WeatherObject
import WeatherData
import retrofit2.converter.gson.GsonConverterFactory
import Main
import Sys
import Clouds
import Coord
import Weather
import Wind
import City
import MainForecast
import Rain
import android.provider.MediaStore.Video
import com.google.gson.reflect.TypeToken
import java.util.jar.Attributes
import forecastItem


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private var fragmentColleactionAdapter: FragmentCollectionAdapter? = null
    private var txt: List<String> = ArrayList<String>()
    private var isFirst: Boolean? = null


    private var txtForecast: String? = null


    override fun onResume() {
        super.onResume()
        fromPref()
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("WeatherObject.main", Gson().toJson(WeatherObject?.main))
        editor.putString("WeatherObject.wind", Gson().toJson(WeatherObject?.wind))
        editor.putString("WeatherObject.weather", Gson().toJson(WeatherObject?.weather))
        editor.putString("WeatherObject.visibility", Gson().toJson(WeatherObject?.visibility))
        editor.putString("WeatherObject.timezone", Gson().toJson(WeatherObject?.timezone))
        editor.putString("WeatherObject.sys", Gson().toJson(WeatherObject?.sys))
        editor.putString("WeatherObject.name", Gson().toJson(WeatherObject?.name))
        editor.putString("WeatherObject.id", Gson().toJson(WeatherObject?.id))
        editor.putString("WeatherObject.dt", Gson().toJson(WeatherObject?.dt))
        editor.putString("WeatherObject.coord", Gson().toJson(WeatherObject?.coord))
        editor.putString("WeatherObject.cod", Gson().toJson(WeatherObject?.cod))
        editor.putString("WeatherObject.clouds", Gson().toJson(WeatherObject?.clouds))
        editor.putString("WeatherObject.base", Gson().toJson(WeatherObject?.base))
        editor.putString("ForecastObject.list", Gson().toJson(ForecastObject?.list))
        editor.putString("ForecastObject.city", Gson().toJson(ForecastObject?.city))
        editor.putString("ForecastObject.cnt", Gson().toJson(ForecastObject?.cnt))
        editor.putString("ForecastObject.cod", Gson().toJson(ForecastObject?.cod))
        editor.putString("ForecastObject.message", Gson().toJson(ForecastObject?.message))
        editor.putBoolean("isFirst", isFirst!!)
        editor.commit()
        println("On Pause")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("On Create")

        fromPref()
        if (isFirst!!) {
            isFirst == false
            println("zamina z pierwszego" + isFirst)
            fromPrefIntoObj()
        } else {
            println("z pref" + isFirst)
            fromPrefIntoObj()
        }




        viewPager = findViewById(R.id.pager)
        viewPager.offscreenPageLimit = 4
        fragmentColleactionAdapter = FragmentCollectionAdapter(supportFragmentManager)
        viewPager.adapter = fragmentColleactionAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.settings) {
//            var i:Intent = Intent(this,PopSettings::class.java)
//            startActivity(i)
            openDialog()
        }
        if (id == R.id.settingsWeather) {
            openDialogWeather()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        var popSettings: PopSettings = PopSettings()
        popSettings.show(supportFragmentManager, "example dialog")
    }

    private fun openDialogWeather() {
        var weatherSettings: WeatherSettings = WeatherSettings()
        weatherSettings.show(supportFragmentManager, "example dialog")
    }

    private fun setUpObject(weatherobject: WeatherObject) {
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

    private fun setUpObject(forecast: ForecastObject?) {
        ForecastObject.list = forecast?.list
        ForecastObject.message = forecast?.message
        ForecastObject.cod = forecast?.cod
        ForecastObject.cnt = forecast?.cnt
        ForecastObject.city = forecast?.city
    }

    private fun toPref() {
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("WeatherObject.main", Gson().toJson(WeatherObject?.main))
        editor.putString("WeatherObject.wind", Gson().toJson(WeatherObject?.wind))
        editor.putString("WeatherObject.weather", Gson().toJson(WeatherObject?.weather))
        editor.putString("WeatherObject.visibility", Gson().toJson(WeatherObject?.visibility))
        editor.putString("WeatherObject.timezone", Gson().toJson(WeatherObject?.timezone))
        editor.putString("WeatherObject.sys", Gson().toJson(WeatherObject?.sys))
        editor.putString("WeatherObject.name", Gson().toJson(WeatherObject?.name))
        editor.putString("WeatherObject.id", Gson().toJson(WeatherObject?.id))
        editor.putString("WeatherObject.dt", Gson().toJson(WeatherObject?.dt))
        editor.putString("WeatherObject.coord", Gson().toJson(WeatherObject?.coord))
        editor.putString("WeatherObject.cod", Gson().toJson(WeatherObject?.cod))
        editor.putString("WeatherObject.clouds", Gson().toJson(WeatherObject?.clouds))
        editor.putString("WeatherObject.base", Gson().toJson(WeatherObject?.base))
        editor.putString("ForecastObject.list", Gson().toJson(ForecastObject?.list))
        editor.putString("ForecastObject.city", Gson().toJson(ForecastObject?.city))
        editor.putString("ForecastObject.cnt", Gson().toJson(ForecastObject?.cnt))
        editor.putString("ForecastObject.cod", Gson().toJson(ForecastObject?.cod))
        editor.putString("ForecastObject.message", Gson().toJson(ForecastObject?.message))
        editor.putBoolean("isFirst", isFirst!!)
        editor.commit()
        println("On Pause")
    }

    fun fromPref() {
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)

        txt += sharedPref.getString("WeatherObject.main", null)
        txt += sharedPref.getString("WeatherObject.wind", null)
        txt += sharedPref.getString("WeatherObject.weather", null)
        txt += sharedPref.getString("WeatherObject.visibility", null)
        txt += sharedPref.getString("WeatherObject.timezone", null)
        txt += sharedPref.getString("WeatherObject.sys", null)
        txt += sharedPref.getString("WeatherObject.name", null)
        txt += sharedPref.getString("WeatherObject.id", null)
        txt += sharedPref.getString("WeatherObject.dt", null)
        txt += sharedPref.getString("WeatherObject.coord", null)
        txt += sharedPref.getString("WeatherObject.cod", null)
        txt += sharedPref.getString("WeatherObject.clouds", null)
        txt += sharedPref.getString("WeatherObject.base", null)
        txt += sharedPref.getString("ForecastObject.list", null)
        txt += sharedPref.getString("ForecastObject.city", null)
        txt += sharedPref.getString("ForecastObject.cnt", null)
        txt += sharedPref.getString("ForecastObject.cod", null)
        txt += sharedPref.getString("ForecastObject.message", null)
        isFirst = sharedPref.getBoolean("isFirst", true)
    }

    fun fromPrefIntoObj() {
        var i = 0
        println(txt)
        while (i < txt.size) {
            when (i) {
                0 -> WeatherObject.main = Gson().fromJson(txt[i], Main::class.java)
                1 -> WeatherObject.wind = Gson().fromJson(txt[i], Wind::class.java)
                2 -> WeatherObject.weather = Gson().fromJson(txt[i], object : TypeToken<List<Weather>>() {}.type)
                3 -> WeatherObject.visibility = Gson().fromJson(txt[i], Int::class.java)
                4 -> WeatherObject.timezone = Gson().fromJson(txt[i], Int::class.java)
                5-> WeatherObject.sys = Gson().fromJson(txt[i], Sys::class.java)
                6 -> WeatherObject.name = Gson().fromJson(txt[i], String::class.java)
                7 -> WeatherObject.id = Gson().fromJson(txt[i], Int::class.java)
                8 -> WeatherObject.dt = Gson().fromJson(txt[i], Int::class.java)
                9 -> WeatherObject.coord = Gson().fromJson(txt[i], Coord::class.java)
                10 -> WeatherObject.cod = Gson().fromJson(txt[i], Int::class.java)
                11 -> WeatherObject.clouds = Gson().fromJson(txt[i], Clouds::class.java)
                12 -> WeatherObject.base = Gson().fromJson(txt[i], String::class.java)
                13 -> ForecastObject.list = Gson().fromJson(txt[i], object : TypeToken<List<forecastItem>>() {}.type)
                14 -> ForecastObject.city = Gson().fromJson(txt[i], City::class.java)
                15 -> ForecastObject.cnt = Gson().fromJson(txt[i], Int::class.java)
                16 -> ForecastObject.cod = Gson().fromJson(txt[i], Int::class.java)
                17 -> ForecastObject.message = Gson().fromJson(txt[i], Double::class.java)
            }
            i++
        }
    }

}
