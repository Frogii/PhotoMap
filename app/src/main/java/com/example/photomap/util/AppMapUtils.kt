package com.example.photomap.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.FRIENDS_CATEGORY
import com.example.photomap.util.Constants.NATURE_CATEGORY
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
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
            return MarkerOptions()
                .position(mapMarkCoordinates)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        bitmapVector
                    )
                ).title(mapMark.name)
        }
    }

    class CustomMapInfoWindowAdapter(context: Context, var mapOfMapMark: Map<String, MapMark>) :
        GoogleMap.InfoWindowAdapter {

        private val customView =
            (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

        private fun setWindowInfo(marker: Marker, view: View) {
            Log.d("mapLog", "markerTITLE" + marker.title)
            val mapMark = mapOfMapMark[marker.title]
            val tvDescription = view.findViewById<TextView>(R.id.textViewInfoWindowDescription)
            val tvDate = view.findViewById<TextView>(R.id.textViewInfoWindowDate)
            val ivPhoto = view.findViewById<ImageView>(R.id.imageViewInfoWindow)
            tvDescription.text = mapMark?.description
            tvDate.text = mapMark?.date?.let { AppDateUtils.changeLongToShortPattern(it) }
            Glide
                .with(view.context)
                .load(mapMark?.url)
                .into(ivPhoto)

        }

        override fun getInfoWindow(marker: Marker): View? {
            return null
        }

        override fun getInfoContents(marker: Marker): View {
            setWindowInfo(marker, customView)
            return customView
        }
    }
}