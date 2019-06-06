package com.example.astroweather

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import android.util.Log
import android.widget.LinearLayout
import forecastItem

class MyItemRecyclerViewAdapter(weatherForecast: List<forecastItem>) :
    RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    var list: List<forecastItem> = weatherForecast

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        var viewHolder: ViewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.i("listasize", list.size.toString())
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Log.i("onbindviewholder", "called")

        Log.i("inrecycle", list[p1].toString())

        p0.date.text = list[p1].dt_txt
        p0.temp.text = list[p1].main.temp.toString()
        p0.humidity.text = list[p1].main.humidity.toString()
        p0.pressure.text = list[p1].main.pressure.toString()
        p0.windSpeed.text = list[p1].wind.speed.toString()

        Log.i("recycledata123", p1.toString())
        Log.i("recycledata123", list[p1].toString())

    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var parentItemLayout: LinearLayout
        var date: TextView
        var temp: TextView
        var humidity: TextView
        var pressure: TextView
        var windSpeed: TextView

        init {
            parentItemLayout = itemView.findViewById(R.id.itemFragment)
            date = itemView.findViewById(R.id.dateItem)
            temp = itemView.findViewById(R.id.tempItem)
            humidity = itemView.findViewById(R.id.humidityItem)
            pressure = itemView.findViewById(R.id.pressureItem)
            windSpeed = itemView.findViewById(R.id.windSpeedItem)
        }

    }
}
