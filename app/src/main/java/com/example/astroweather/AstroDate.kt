package com.example.astroweather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.*

class AstroDate : ViewModel() {
    private val dateNow = MutableLiveData<Date>()


    fun init() {
        dateNow.value = Date()
    }

    fun getDate(): LiveData<Date> {
        return dateNow
    }
}
