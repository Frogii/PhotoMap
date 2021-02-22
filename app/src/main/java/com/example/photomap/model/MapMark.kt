package com.example.photomap.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "map_mark_items")
data class MapMark(
    @PrimaryKey()
    val name: String = "",
    var url: String = "",
    val date: String = "9.01.2021",
    var description: String = "Best photo",
    var category: String = "Default",
    var imageLatitude: Double = 0.0,
    var imageLongitude: Double = 0.0
) : Serializable
