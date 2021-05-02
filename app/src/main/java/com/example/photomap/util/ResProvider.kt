package com.example.photomap.util

import android.content.ContentResolver
import android.content.Context

class ResProvider(private val appContext: Context) {

    fun getContentResolver(): ContentResolver {
        return appContext.contentResolver
    }
}