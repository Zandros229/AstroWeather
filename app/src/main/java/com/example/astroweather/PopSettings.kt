package com.example.astroweather

import android.R.layout.simple_spinner_item
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import java.lang.Exception
import kotlin.math.absoluteValue

class PopSettings : AppCompatDialogFragment() {

    lateinit var longitudeSettings: EditText
    lateinit var latitudeSettings: EditText
    lateinit var settingsSpinner: Spinner
    val min = 60


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity!!.layoutInflater
        var view: View = inflater.inflate(R.layout.activity_pop_settings, null)
        builder.setView(view)
            .setNegativeButton("cancel", { dialogInterface: DialogInterface, i: Int ->

            })
            .setPositiveButton("ok", { dialog: DialogInterface?, which: Int ->
                var longitude: String = longitudeSettings.text.toString()
                var latitude: String = latitudeSettings.text.toString()
                var validatedData: List<Double> = validateData(longitude, latitude)
                Log.i("new longitude", validatedData[0].toString())
                Log.i("new latitude", validatedData[1].toString())
                Config.longitudeSafe = validatedData[0]
                Config.latitudeSafe = validatedData[1]

                Log.i("config longi", Config.longitudeSafe.toString())
                Log.i("config latit", Config.latitudeSafe.toString())

            })

        longitudeSettings = view.findViewById(R.id.longitudeSettings)
        latitudeSettings = view.findViewById(R.id.latitudeSettings)
        settingsSpinner = view.findViewById(R.id.settingsSpinner)
        var array = arrayListOf<String>("1", "2", "5", "10", "15", "30", "60")
        // Create an ArrayAdapter using a simple spinner layout and languages array
        settingsSpinner.adapter = ArrayAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, array)
        // Set layout to use when the list of choices appear
        settingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> Config.updateTime = 1 * min
                    1 -> Config.updateTime = 2 * min
                    2 -> Config.updateTime = 5 * min
                    3 -> Config.updateTime = 10 * min
                    4 -> Config.updateTime = 15 * min
                    5 -> Config.updateTime = 30 * min
                    6 -> Config.updateTime = 60 * min
                }
                settingsSpinner.setSelection(position)
            }
        }

        return builder.create()
    }

    fun validateData(longitude: String, latitude: String): List<Double> {
        var validatedData: MutableList<Double> = mutableListOf(0.0, 0.0)
        try {
            if(longitude.toDouble().absoluteValue>180||latitude.toDouble().absoluteValue>90)
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

}
