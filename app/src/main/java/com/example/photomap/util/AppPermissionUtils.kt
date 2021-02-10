package com.example.photomap.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AppPermissionUtils {

    companion object {
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
                        showDialog(
                            "External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } else {
                        ActivityCompat
                            .requestPermissions(
                                (context as Activity),
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
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

        private fun showDialog(
            msg: String, context: Context?,
            permission: String
        ) {
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("Permission necessary")
            alertBuilder.setMessage("$msg permission is necessary")
            alertBuilder.setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!, arrayOf(permission),
                        Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                })
            val alert: AlertDialog = alertBuilder.create()
            alert.show()
        }
    }


}