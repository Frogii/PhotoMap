package com.example.photomap.util

import java.text.SimpleDateFormat
import java.util.*

class AppDateUtils {

    companion object {
        const val detailsDatePattern = "yyyy.mm.dd"
        const val mapMarkPhotoNamePattern = "yyyyMMdd_hh:mm:ss"

        fun formatDate(date: Date, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        }
    }
}