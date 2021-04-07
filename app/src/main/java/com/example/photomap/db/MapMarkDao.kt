package com.example.photomap.db

import androidx.room.*
import com.example.photomap.model.MapMark

@Dao
interface MapMarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMapMark(item: MapMark)

    @Query("SELECT*FROM map_mark_items WHERE category IN (:filter)")
    suspend fun getAllMapMarks(filter: MutableList<String>): List<MapMark>

    @Delete
    suspend fun deleteMapMark(mapMark: MapMark)
}