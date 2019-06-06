package com.example.astroweather

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import java.text.SimpleDateFormat
import java.util.*
import android.os.StrictMode
import android.R.id.edit
import android.content.SharedPreferences
import com.google.gson.Gson
import com.example.astroweather.WeatherObject as WeatherObject


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private var fragmentColleactionAdapter: FragmentCollectionAdapter? = null
    private var txt: String? = null

    private var fragmentColleactionAdapterForecast: FragmentCollectionAdapter? = null
    private var txtForecast: String? = null


    override fun onResume() {
        super.onResume()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        txt = sharedPref.getString("dane", "")
        val sharedPrefForecast = this.getPreferences(Context.MODE_PRIVATE)
        txtForecast = sharedPrefForecast.getString("daneForecast", "")
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("data", Gson().toJson(WeatherObject))
        editor.commit()
        val sharedPrefForecast = this.getPreferences(Context.MODE_PRIVATE)
        val editorForecast = sharedPrefForecast.edit()
        editorForecast.putString("data", Gson().toJson(ForecastObject))
        editorForecast.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (txt != null) {
            var weatherobject = Gson().fromJson(txt, WeatherObject::class.java)
            setUpObject(weatherobject!!)
        }
        if (txtForecast != null) {
            var forecast = Gson().fromJson(txt, ForecastObject::class.java)
            setUpObject(forecast!!)
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
        if(id==R.id.settingsWeather){
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
        ForecastObject.list=forecast?.list
        ForecastObject.message=forecast?.message
        ForecastObject.cod=forecast?.cod
        ForecastObject.cnt=forecast?.cnt
        ForecastObject.city=forecast?.city


    }
}
