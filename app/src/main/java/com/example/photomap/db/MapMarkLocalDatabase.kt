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

        @Volatile
        private var instance: MapMarkLocalDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MapMarkLocalDatabase::class.java, "MapMarkDB.db"
            ).build()
    }
}