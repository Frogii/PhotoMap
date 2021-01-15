package com.example.photomap.model

import java.io.Serializable


data class MapMark(
    val name: String = "",
    val url: String = "",
    val date: String = "9.01.2021",
    var description: String = "Best photo",
    var categery: String = "Default",
    var imageLatitude: Double = 0.0,
    var imageLongitude: Double = 0.0
): Serializable
