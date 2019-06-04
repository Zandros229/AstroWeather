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
    private var txt: String?= null


    override fun onResume() {
        super.onResume()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        txt = sharedPref.getString("dane", "")
    }
    override fun onPause() {
        super.onPause()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("data",Gson().toJson(WeatherObject))
        editor.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(txt!=null) {
            var weatherobject = Gson().fromJson(txt, WeatherObject::class.java)
            setUpObject(weatherobject!!)
        }


        var isTablet : Boolean = resources.getBoolean(R.bool.isTablet)
        if(!isTablet){
            viewPager = findViewById(R.id.pager)
            viewPager.offscreenPageLimit=4
            fragmentColleactionAdapter = FragmentCollectionAdapter(supportFragmentManager)
            viewPager.adapter = fragmentColleactionAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id.settings){
//            var i:Intent = Intent(this,PopSettings::class.java)
//            startActivity(i)
            openDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        var popSettings: PopSettings = PopSettings()
        popSettings.show(supportFragmentManager, "example dialog")
    }
    private fun setUpObject(weatherobject: WeatherObject){
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
}
