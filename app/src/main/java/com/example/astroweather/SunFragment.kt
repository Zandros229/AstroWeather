package com.example.astroweather


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import kotlinx.android.synthetic.main.fragment_moon.*
import kotlinx.android.synthetic.main.fragment_sun.*
import java.lang.Exception
import java.math.MathContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class SunFragment : Fragment() {

    lateinit var fragmentView: View
    lateinit var sunRise: TextView
    lateinit var sunSet: TextView
    lateinit var sunCivRise: TextView
    lateinit var sunCivSet: TextView
    lateinit var longitudeSun: TextView
    lateinit var latitudeSun: TextView
    lateinit var sunRiseAzimuth: TextView
    lateinit var sunSetAzimuth: TextView
    lateinit var model: AstroDate


    fun initTextViews() {
        sunRise = fragmentView.findViewById(R.id.sunRise)
        sunSet = fragmentView.findViewById(R.id.sunSet)
        sunCivRise = fragmentView.findViewById(R.id.sunCivRise)
        sunCivSet = fragmentView.findViewById(R.id.sunCivSet)
        longitudeSun = fragmentView.findViewById(R.id.longitudeSun)
        latitudeSun = fragmentView.findViewById(R.id.latitudeSun)
        sunRiseAzimuth = fragmentView.findViewById(R.id.sunRaiseAzimuth)
        sunSetAzimuth = fragmentView.findViewById(R.id.sunSetAzimuth)
    }

    lateinit var astroCalculator: AstroCalculator
    lateinit var astroCalculatorLocation: AstroCalculator.Location
    var latitudeData: Double = Config.latitudeSafe
    var longitudeData: Double = Config.longitudeSafe
    val astroDateTime = AstroDateTime()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_sun, container, false)

        initTextViews()

        astroCalculatorLocation = AstroCalculator.Location(latitudeData, longitudeData)

        model = ViewModelProviders.of(this).get(AstroDate::class.java)
        model.init()

        updateclock()
        update()

        Log.i("AstroCalc SUN RISE", astroCalculator.sunInfo.sunrise.toString())
        Log.i("AstroCalc SUN SET", astroCalculator.sunInfo.sunset.toString())
        Log.i("AstroCalc CIV SUN RISE", astroCalculator.sunInfo.twilightMorning.toString())
        Log.i("AstroCalc CIV SUN SET", astroCalculator.sunInfo.twilightEvening.toString())

        //clock part
        val sdf = SimpleDateFormat("HH:mm:ss")

        Thread(Runnable {
            var index = 0
            while (true) {

                var currentDate = sdf.format(Date())
                currentDate = sdf.format(Date())

                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            if (longitudeData != Config.longitudeSafe || latitudeData != Config.latitudeSafe) {
                                latitudeData = Config.latitudeSafe
                                longitudeData = Config.longitudeSafe
                                updateclock()
                                update()
                            }
                            if (index >= Config.updateTime) {
                                updateclock()
                                update()
                                println(index.toString() + " " + Config.updateTime + "  XDDDDD Moon")
                                index = 0
                            }
                            try {
                                actualTimeSun.text = currentDate.toString()
                            } catch (e: Exception) {
                                println("Cos sie zjebalo XDDDD")
                            }
                        }
                    }
                } catch (e:Exception) {
                    if(activity != null){
                        activity!!.finish()
                    }
                }


                Thread.sleep(1000)
                index++
            }

        }).start()

        return fragmentView
    }

    public fun update() {
        astroCalculatorLocation = AstroCalculator.Location(latitudeData, longitudeData)
        astroCalculator = AstroCalculator(astroDateTime, astroCalculatorLocation)
        longitudeSun.text = longitudeData.toString()
        latitudeSun.text = latitudeData.toString()
        var temp: List<String>? = null
        temp = astroCalculator.sunInfo.sunrise.toString().split(" ")
        sunRise.text = temp[1]
        temp = astroCalculator.sunInfo.sunset.toString().split(" ")
        sunSet.text = temp[1]
        temp = astroCalculator.sunInfo.twilightMorning.toString().split(" ")
        sunCivRise.text = temp[1]
        temp = astroCalculator.sunInfo.twilightEvening.toString().split(" ")
        sunCivSet.text = temp[1]
        temp =
            astroCalculator.sunInfo.azimuthRise.toBigDecimal(MathContext(4, RoundingMode.HALF_UP)).toString()
                .split(" ")
        sunRiseAzimuth.text = temp[0]
        temp =
            astroCalculator.sunInfo.azimuthSet.toBigDecimal(MathContext(4, RoundingMode.HALF_UP)).toString()
                .split(" ")
        sunSetAzimuth.text = temp[0]

    }

    public fun updateclock() {
        astroDateTime.day = checkNotNull(model.getDate().value?.day)
        astroDateTime.month = checkNotNull(model.getDate().value?.month)
        astroDateTime.year = checkNotNull(model.getDate().value?.year) + 1900
        astroDateTime.hour = checkNotNull(model.getDate().value?.hours)
        astroDateTime.minute = checkNotNull(model.getDate().value?.minutes)
        astroDateTime.second = checkNotNull(model.getDate().value?.seconds)
        astroDateTime.timezoneOffset = checkNotNull(model.getDate().value?.timezoneOffset)
        astroDateTime.isDaylightSaving = true
    }
}
