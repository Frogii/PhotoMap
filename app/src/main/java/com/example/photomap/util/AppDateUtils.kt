package com.example.photomap.util

import java.text.SimpleDateFormat
import java.util.*

class AppDateUtils {

    companion object {
        const val longPhotoDatePattern = "MMMM d, yyyy - hh:mm a"
        const val shortPhotoDatePattern = "yyyy.MM.dd"
        const val mapMarkPhotoNamePattern = "yyyyMMdd_hh:mm:ss"

        fun formatDate(date: Date, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        }

        fun changeLongToShortPattern(date: String): String {
            return try {
                val formatterIn = SimpleDateFormat(longPhotoDatePattern)
                val formatterOut = SimpleDateFormat(shortPhotoDatePattern)
                formatterOut.format(formatterIn.parse(date))
            } catch (e: Exception) {
                date
            }
        }
    }
}