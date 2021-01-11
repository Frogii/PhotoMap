package com.example.photomap.model

data class MapMark(
    val name: String = "",
    val url: String = "",
    var description: String = "Best photo",
    var categery: String = "Default",
    var imageLatitude: Double = 0.0,
    var imageLongitude: Double = 0.0
)
