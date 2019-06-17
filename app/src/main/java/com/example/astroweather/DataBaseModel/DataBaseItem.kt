package com.example.astroweather.DataBaseModel


import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "items")
class DataBaseItem {
    @PrimaryKey
    public var id: Long = 0
    public var name: String? = null
    public var temp: Double? = null
    public var date: String? = null
}