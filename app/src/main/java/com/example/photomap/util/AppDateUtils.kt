package com.example.photomap.util

import java.text.SimpleDateFormat
import java.util.*

class AppDateUtils {

    companion object {
        const val detailsDatePattern = "yyyy.mm.dd"

        fun formatDate(date: Date, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        }
    }
}