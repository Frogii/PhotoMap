package com.example.photomap.model

import java.util.*

data class MapMark(
    var photoUrl: String,
    var description: String,
    var date: Date,
    var category: String
)
