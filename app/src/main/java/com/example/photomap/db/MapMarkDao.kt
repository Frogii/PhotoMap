package com.example.photomap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photomap.model.MapMark

@Dao
interface MapMarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMapMark(item: MapMark)

    @Query("SELECT*FROM map_mark_items WHERE category IN (:filter)")
    suspend fun getAllMapMarks(filter: MutableList<String>): List<MapMark>
}