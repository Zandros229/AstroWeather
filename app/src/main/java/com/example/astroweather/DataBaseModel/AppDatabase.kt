package com.example.astroweather.DataBaseModel



import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase



import com.example.astroweather.DataBaseModel.ItemDAO


@Database(entities = [DataBaseItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val itemDAO: ItemDAO
}