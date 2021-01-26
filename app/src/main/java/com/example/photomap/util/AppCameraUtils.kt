package com.example.photomap.util

import android.content.Context
import android.os.Environment
import com.example.photomap.R
import java.io.File
import java.util.*

class AppCameraUtils {
    companion object {

        fun getPhotoFile(fileName: String, context: Context): File {
            val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, context.getString(R.string.end_of_file), storageDirectory)
        }

        fun createPhotoName(context: Context): String {
            val timeStamp = AppDateUtils.formatDate(Date(), AppDateUtils.mapMarkPhotoNamePattern)
            return context.getString(R.string.start_of_file) + timeStamp
        }
    }
}