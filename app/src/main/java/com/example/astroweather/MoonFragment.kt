package com.example.astroweather

import android.arch.lifecycle.ViewModelProviders
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
import java.text.SimpleDateFormat
import java.util.*


class MoonFragment : Fragment() {

    lateinit var fragmentView: View
    lateinit var longitudeMoon: TextView
    lateinit var latitudeMoon: TextView
    lateinit var moonRise: TextView
    lateinit var moonSet: TextView
    lateinit var nextNewMoon: TextView
    lateinit var nextFullMoon: TextView
    lateinit var illumination: TextView
    lateinit var synodicDay: TextView
    lateinit var model: AstroDate
    fun initTextViews() {
        longitudeMoon = fragmentView.findViewById(R.id.longitudeMoon)
        latitudeMoon = fragmentView.findViewById(R.id.latitudeMoon)
        moonRise = fragmentView.findViewById(R.id.moonRise)
        moonSet = fragmentView.findViewById(R.id.moonSet)
        nextNewMoon = fragmentView.findViewById(R.id.nextNewMoon)
        nextFullMoon = fragmentView.findViewById(R.id.nextFullMoon)
        illumination = fragmentView.findViewById(R.id.illumination)
        synodicDay = fragmentView.findViewById(R.id.synodicDay)
    }

    lateinit var astroCalculator: AstroCalculator
    lateinit var astroCalculatorLocation: AstroCalculator.Location
    var latitudeData: Double = Config.latitudeSafe
    var longitudeData: Double = Config.longitudeSafe
    val astroDateTime = AstroDateTime()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_moon, container, false)

        initTextViews()


        astroCalculatorLocation = AstroCalculator.Location(latitudeData, longitudeData)


        model = ViewModelProviders.of(this).get(AstroDate::class.java)
        model.init()

        updateclock()
        update()


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
                                index = 0
                            }
                            try {
                                actualTimeMoon.text = currentDate.toString()
                            } catch (e: Exception) {

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
        longitudeMoon.text = longitudeData.toString()
        latitudeMoon.text = latitudeData.toString()
        println(astroDateTime.toString() + " XDDDD")
        var temp: List<String>? = null
        temp = astroCalculator.moonInfo.moonrise.toString().split(" ")
        moonRise.text = temp[1]
        println(astroCalculator.moonInfo.moonset.toString().split(" ")+ " XDDDDD MoonSet")
        temp = astroCalculator.moonInfo.moonset.toString().split(" ")

        moonSet.text = temp[1]
        temp = astroCalculator.moonInfo.nextNewMoon.toString().split(" ")
        println(astroCalculator.moonInfo.nextNewMoon.toString() + " XDDDD")
        nextNewMoon.text = temp[0]
        temp = astroCalculator.moonInfo.nextFullMoon.toString().split(" ")
        nextFullMoon.text = temp[0]
        var tempIllumination: Double = astroCalculator.moonInfo.illumination
        tempIllumination = tempIllumination * 100
        var days = (astroCalculator.moonInfo.nextNewMoon.day - astroDateTime.day).toDouble()
        if (days < 0)
            days += 29.531
        synodicDay.text = days.toString()

        illumination.text = tempIllumination.toString().substring(0, 4) + "%"
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
