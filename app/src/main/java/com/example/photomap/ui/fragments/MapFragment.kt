package com.example.photomap.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.ui.MainActivity
import com.example.photomap.ui.MainViewModel
import com.example.photomap.ui.dialog.ChoosePhotoDialog
import com.example.photomap.ui.dialog.DialogClickListener
import com.example.photomap.util.AppCameraUtils
import com.example.photomap.util.AppMapUtils
import com.example.photomap.util.Constants.FILE_PROVIDER_PATH
import com.example.photomap.util.Constants.MAP_MARK_ITEM
import com.example.photomap.util.Constants.REQUEST_CODE_IMAGE_PICK
import com.example.photomap.util.Constants.REQUEST_CODE_TAKE_PHOTO
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.collection.LLRBNode
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mapViewBundle: Bundle? = null
    private lateinit var map: GoogleMap
    private lateinit var mainViewModel: MainViewModel
    private var photoFile: File? = null
    private lateinit var photoUri: Uri
    private var longClickPhotoLatLng = LatLng(0.0, 0.0)
    private var listOfMapMarks: List<MapMark> = listOf()
    private var mapOfMapMarks: MutableMap<String, MapMark> = mutableMapOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: Location
    private var followButtonState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewBundle = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.getAllMapMarks()

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        floatingButtonPhoto.setOnClickListener {
            ChoosePhotoDialog(activity as MainActivity, object : DialogClickListener {
                override fun chooseImage() {
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                        .also {
                            startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
                        }
                }

                override fun takePhoto() {
                    photoFile = activity?.let { activity ->
                        AppCameraUtils.getPhotoFile(
                            AppCameraUtils.createPhotoName(activity),
                            activity
                        )
                    }
                    val fileProvider = photoFile?.let { file ->
                        activity?.let { activity ->
                            FileProvider.getUriForFile(
                                activity,
                                FILE_PROVIDER_PATH,
                                file
                            )
                        }
                    }
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                        it.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                        startActivityForResult(it, REQUEST_CODE_TAKE_PHOTO)
                    }
                }
            }).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //taking photo from camera
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            photoUri = Uri.fromFile(photoFile)
            photoFile?.name?.let {
                mainViewModel.uploadMapMark(
                    photoUri, it.substring(0, 22),
                    myLocation.latitude,
                    myLocation.longitude
                )
            }
        } else if (requestCode == REQUEST_CODE_IMAGE_PICK) {
            //taking photo from gallery
            data?.data?.let {
                Log.d("myLog", "taking photo from gallery")
                val file = it
                val fileName =
                    this.activity?.let { activity -> AppCameraUtils.createPhotoName(activity) }
                if (fileName != null) {
                    mainViewModel.uploadMapMark(
                        file,
                        fileName,
                        myLocation.latitude,
                        myLocation.longitude
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_categories -> findNavController().navigate(
                R.id.action_mapFragment_to_categoriesFragment
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val LOCATION_PERMISSION_REQUEST = 1
    }


    override fun onMapReady(p0: GoogleMap) {
        val zoomLevel = 12f
        map = p0
        getMyLocation()
        if (activity?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location ->
                    myLocation = location
                    val myLatLong = LatLng(location.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLong, zoomLevel))
                }
        }
//        val myLatLng = LatLng(myLocation.latitude, myLocation.longitude)
        mainViewModel.followButtonLiveDataState.observe(viewLifecycleOwner, {
            followButtonState = it
            if (followButtonState) {
                CoroutineScope(Dispatchers.IO).launch {
                    while (followButtonState) {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location ->
                                myLocation = location
                                map.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            location.latitude,
                                            location.longitude
                                        ), zoomLevel
                                    )
                                )
                            }
                        delay(5000)
                    }
                }
                AppMapUtils.changeMapUiState(map, followButtonState)
                floatingButtonFollow.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.floating_button_enable))
            } else {
                AppMapUtils.changeMapUiState(map, followButtonState)
                floatingButtonFollow.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.floating_button_disable))
            }
        })
        map.uiSettings.isMyLocationButtonEnabled = false
        mainViewModel.dataList.observe(viewLifecycleOwner, { it ->
            listOfMapMarks = it
            for (mapMark in listOfMapMarks) {
                map.addMarker(AppMapUtils.setMarkerOptions(mapMark, this.requireContext()))
                mapOfMapMarks[mapMark.name] = mapMark
            }
        })
        val minsk = LatLng(53.893009, 27.567444)
        map.addMarker(MarkerOptions().position(minsk).title("Minsk"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(minsk, zoomLevel))
        map.setInfoWindowAdapter(this.activity?.let {
            AppMapUtils.CustomMapInfoWindowAdapter(
                it,
                mapOfMapMarks
            )
        })
        map.setOnMapLongClickListener {
            longClickPhotoLatLng = LatLng(it.latitude, it.longitude)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(longClickPhotoLatLng, zoomLevel))
            ChoosePhotoDialog(activity as MainActivity, object : DialogClickListener {
                override fun chooseImage() {
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                        .also {
                            startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
                        }
                }

                override fun takePhoto() {
                    photoFile = this@MapFragment.activity?.let { activity ->
                        AppCameraUtils.getPhotoFile(
                            AppCameraUtils.createPhotoName(activity),
                            activity
                        )
                    }
                    val fileProvider = photoFile?.let { file ->
                        this@MapFragment.activity?.let { activity ->
                            FileProvider.getUriForFile(
                                activity,
                                FILE_PROVIDER_PATH,
                                file
                            )
                        }
                    }
                    Intent(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    ).also {
                        it.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                        startActivityForResult(it, REQUEST_CODE_TAKE_PHOTO)
                    }
                }
            }).show()
            mainViewModel.getAllMapMarks()
        }

        map.setOnInfoWindowClickListener { marker ->
            startActivity(Intent(this.context, DetailsActivity::class.java).also {
                it.putExtra(MAP_MARK_ITEM, mapOfMapMarks[marker.title])
            })
        }

        map.setOnMarkerClickListener {
            it.showInfoWindow()
            if (it.isInfoWindowShown) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    it.showInfoWindow()
                }
            }
            true
        }

        floatingButtonFollow.setOnClickListener {
            mainViewModel.followButtonLiveDataState.value = !followButtonState
            if (followButtonState) {
                getMyLocation()
                Log.d("mapLog", "getloc")
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(myLocation.latitude, myLocation.longitude), zoomLevel
                    )
                )
            }
        }
    }

    private fun getMyLocation() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location ->
                    myLocation = location
                }

        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getMyLocation()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.location_permission_not_granted),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}