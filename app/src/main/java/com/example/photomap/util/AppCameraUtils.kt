package com.example.photomap.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.*

class AppCameraUtils {
    companion object {

        fun getPhotoFile(fileName: String, context: Context): File {
            val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, ".jpg", storageDirectory)
        }

        fun createPhotoName(): String {
            val timeStamp = AppDateUtils.formatDate(Date(), AppDateUtils.mapMarkPhotoNamePattern)
            return "JPEG_${timeStamp}"
        }
    }
}