package com.example.astroweather

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import java.io.IOException
import java.lang.Exception
import kotlin.math.absoluteValue


class WeatherSettings : AppCompatDialogFragment() {

    lateinit var nameSettings: EditText
    lateinit var spiner: Spinner
    lateinit var switch: Switch
    val min = 5


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity!!.layoutInflater

        var view: View = inflater.inflate(R.layout.activity_weather_settings, null)
        builder.setView(view)
            .setNegativeButton("cancel", { dialogInterface: DialogInterface, i: Int ->

            })
            .setPositiveButton("ok", { dialog: DialogInterface?, which: Int ->
                var nameTemp: String = nameSettings.text.toString()

                if(nameTemp.length>2)
                    Config.cityName = nameTemp
                if(switch.isChecked){
                    Config.units = "K"
                    println("Zmiana na K")
                } else {
                    Config.units = "C"
                    println("Zmiana na C")
                }
                if(!isOnline()){
                    Toast.makeText(this.context,"Connect to interent to get weather info",Toast.LENGTH_LONG).show()
                }
            })

        nameSettings = view.findViewById(R.id.NameSettings)
        switch = view.findViewById(R.id.switchWeatherSettings)
        spiner = view.findViewById(R.id.weatherSpinner)
        var array = arrayListOf<String>("London", "Paris", "Warsaw")
        spiner.adapter = ArrayAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, array)
        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> Config.cityName = "London"
                    1 -> Config.cityName = "Paris"
                    2 -> Config.cityName = "Warsaw"

                }

            }
        }

        return builder.create()
    }

    fun validateData(longitude: String, latitude: String): List<Double> {
        var validatedData: MutableList<Double> = mutableListOf(0.0, 0.0)
        try {
            if (longitude.toDouble().absoluteValue > 180 || latitude.toDouble().absoluteValue > 90)
                throw Exception("Wrong Data")
            validatedData.set(0, longitude.toDouble())
            validatedData.set(1, latitude.toDouble())
        } catch (e: Exception) {
            Toast.makeText(this.context, "Wrong Data", Toast.LENGTH_LONG).show()
            validatedData.set(0, Config.longitudeSafe)
            validatedData.set(1, Config.latitudeSafe)
        }
        return validatedData
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

}

