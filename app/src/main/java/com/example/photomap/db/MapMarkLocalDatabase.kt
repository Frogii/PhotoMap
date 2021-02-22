package com.example.photomap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.photomap.model.MapMark

@Database(
    entities = [MapMark::class],
    version = 1
)
abstract class MapMarkLocalDatabase : RoomDatabase() {

    abstract fun getMapMarkDao(): MapMarkDao

    companion object {
        fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MapMarkLocalDatabase::class.java, "MapMarkDB.db"
            ).build()
    }
}