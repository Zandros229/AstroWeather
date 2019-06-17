package com.example.astroweather.DataBaseModel


import android.arch.persistence.room.*
import java.util.List;

@Dao
interface ItemDAO {
    @Insert
    fun insert(vararg items: DataBaseItem)

    @Update
    fun update(vararg items: DataBaseItem)

    @Delete
    fun delete(item: DataBaseItem)

    @Query("SELECT * FROM items")
    fun getItems(): List<DataBaseItem>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Long?): DataBaseItem
}