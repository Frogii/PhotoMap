package com.example.photomap.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.photomap.ui.MainActivity
import com.example.photomap.util.Constants.PERMISSIONS_REQUEST
import com.example.photomap.util.Constants.PERMISSIONS_REQUEST_FIND_MY_LOCATION
import com.example.photomap.util.Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE

class AppPermissionUtils {

    companion object {

        fun requestAllPermissions(context: Context){
            ActivityCompat
                .requestPermissions(
                    context as MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST
                )
        }

        fun checkReadStoragePermission(context: Context): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        ActivityCompat
                            .requestPermissions(
                                (context as Activity),
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                            )
                    }
                    false
                } else {
                    true
                }
            } else {
                true
            }
        }

        fun checkMyLocationPermission(context: Context): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            (context as Activity),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSIONS_REQUEST_FIND_MY_LOCATION
                        )
                    }
                    false
                } else {
                    true
                }
            } else {
                true
            }
        }
    }

}