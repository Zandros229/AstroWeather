package com.example.astroweather


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.GraphView
import java.lang.Exception
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import forecastItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.parse
import com.jjoe64.graphview.ValueDependentColor
import com.example.astroweather.DataBaseModel.AppDatabase
import com.example.astroweather.DataBaseModel.ItemDAO
import com.example.astroweather.DataBaseModel.DataBaseItem


class Chart : Fragment() {

    lateinit var fragmentView: View
    lateinit var unit: String
    lateinit var graph: GraphView
    var started = false
    var temp = 0.0
   // lateinit var database: AppDatabase

    private fun init() {
        graph = fragmentView.findViewById(R.id.graph)
        graph.title = "Weather for 5 days"
        graph.titleColor = Color.BLUE
        graph.titleTextSize = 120.toFloat()
        graph.viewport.backgroundColor = Color.parseColor("#738DAF")
//        database = Room.databaseBuilder(fragmentView.context, AppDatabase::class.java, "mydb")
//            .allowMainThreadQueries()
//            .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_chart, container, false)
        init()

        unit = Config.units
        Thread(Runnable {
            var index = 0
            while (true) {


                try {
                    if (activity != null) {
                        activity!!.runOnUiThread {

                            if (ForecastObject.city?.name != Config.cityName) {
                                update()
                                //weatherForecast?.city?.name=Config.cityName
                            }
                            if (unit != Config.units) {
                                update()
                                unit = Config.units
                            }
                            if (WeatherObject.name != ForecastObject.city?.name)
                                update()
                            if (graph.series.size == 0)
                                update()

                            if (index >= Config.updateTimeWeather) {
                                update()
                                index = 0
                            }
                            if (temp != ForecastObject?.list?.get(0)?.main?.temp!!)
                                update()


                        }
                    }

                } catch (e: Exception) {
                    if (activity != null) {
                        activity!!.finish()
                    }
                }


                Thread.sleep(1000)
                index++
            }
        }).start()

        return fragmentView
    }


    private fun update() {
        lateinit var list: Array<forecastItem>
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var series: BarGraphSeries<DataPoint>
        temp = ForecastObject?.list?.get(0)?.main?.temp!!
        series = BarGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(
                    0.toDouble(),
                    ForecastObject?.list?.get(0)?.main?.temp!!
                ),
                DataPoint(
                    1.toDouble(),
                    ForecastObject?.list?.get(1)?.main?.temp!!
                ),
                DataPoint(
                    2.toDouble(),
                    ForecastObject?.list?.get(2)?.main?.temp!!
                ),
                DataPoint(
                    3.toDouble(),
                    ForecastObject?.list?.get(3)?.main?.temp!!
                ),
                DataPoint(
                    4.toDouble(),
                    ForecastObject?.list?.get(4)?.main?.temp!!
                )
            )
        )
        updateDB()
        println("in update chart")
        graph.removeAllSeries()


        //series.setSpacing(50)
        // set manual X bounds

        series.valueDependentColor =
            ValueDependentColor { data -> Color.rgb(data.x.toInt() * 255 / 4, Math.abs(data.y * 255 / 6).toInt(), 100) }

        graph.getViewport().setYAxisBoundsManual(true)
        if (Config.units == "K") {
            graph.getViewport().setMinY(260.0)
            graph.getViewport().setMaxY(310.0)
        }
        if (Config.units == "C") {
            graph.getViewport().setMinY(-20.0)
            graph.getViewport().setMaxY(40.0)
        }
        // draw values on top
        series.setDrawValuesOnTop(true)
        series.setValuesOnTopColor(Color.BLUE)
        graph.addSeries(series)
    }

    private fun updateDB() {
//        val itemDAO = database.itemDAO
//        val item = DataBaseItem()
//        for (element in ForecastObject.list!!) {
//            item.name = ForecastObject.city?.name
//            item.temp = element.main?.temp
//            item.date = element.dt_txt
//            itemDAO.insert(item)
//        }


//        val items = itemDAO.getItems()
//        println(items)
    }

}
