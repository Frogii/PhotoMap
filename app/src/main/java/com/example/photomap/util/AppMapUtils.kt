package com.example.photomap.util

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.FRIENDS_CATEGORY
import com.example.photomap.util.Constants.NATURE_CATEGORY
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

class AppMapUtils {

    companion object {
        fun setMarkerOptions(mapMark: MapMark, context: Context): MarkerOptions {
            val mapMarkCoordinates = com.google.android.gms.maps.model.LatLng(
                mapMark.imageLatitude,
                mapMark.imageLongitude
            )
            val markerResourcePath = when (mapMark.category) {
                NATURE_CATEGORY -> R.drawable.ic_place_nature
                FRIENDS_CATEGORY -> R.drawable.ic_place_friends
                else -> R.drawable.ic_place_default
            }
            val bitmapVector = AppCompatResources.getDrawable(context, markerResourcePath)
                ?.toBitmap()
            return MarkerOptions().position(mapMarkCoordinates).icon(
                BitmapDescriptorFactory.fromBitmap(
                    bitmapVector
                )
            )
        }
    }
}