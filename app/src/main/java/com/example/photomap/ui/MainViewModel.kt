package com.example.photomap.ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomap.model.MapMark
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.AppDateUtils
import com.example.photomap.util.Constants.DEFAULT_CATEGORY
import com.example.photomap.util.Constants.FRIENDS_CATEGORY
import com.example.photomap.util.Constants.MAP_MARK_FIELD_CATEGORY
import com.example.photomap.util.Constants.NATURE_CATEGORY
import com.example.photomap.util.Constants.NULL_CATEGORY
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class MainViewModel(private val mapMarkRepository: MapMarkRepository) : ViewModel() {

    private val mapMarkList = mutableListOf<MapMark>()
    val dataList: MutableLiveData<List<MapMark>> = MutableLiveData()
    private val categoryList =
        mutableListOf(FRIENDS_CATEGORY, NATURE_CATEGORY, DEFAULT_CATEGORY, NULL_CATEGORY)
    val categoryLiveDataList: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val checkBoxStateMap = mutableMapOf(
        Pair(FRIENDS_CATEGORY, true),
        Pair(NATURE_CATEGORY, true),
        Pair(DEFAULT_CATEGORY, true)
    )
    val checkBoxLiveDataStateMap: MutableLiveData<MutableMap<String, Boolean>> = MutableLiveData()
    val followButtonLiveDataState: MutableLiveData<Boolean> = MutableLiveData()

    init {
//        syncLocalDatabaseWithFirebase()
        followButtonLiveDataState.value = true
        categoryLiveDataList.value = categoryList
        checkBoxLiveDataStateMap.postValue(checkBoxStateMap)
    }

    fun uploadMapMark(imageFile: Uri, imageName: String, imgLat: Double, imgLng: Double) {
        viewModelScope.launch {
            val markForDB = MapMark(
                name = imageName,
                url = imageFile.toString(),
                date = AppDateUtils.formatDate(
                    Date(), AppDateUtils.longPhotoDatePattern
                ),
                imageLatitude = imgLat,
                imageLongitude = imgLng
            )
            mapMarkRepository.addMarkToDB(markForDB)
            try {
                imageName.let {
                    mapMarkRepository.uploadPhotoToFireStorage(imageFile, imageName)
                    val imageUrl = mapMarkRepository.getImageUrlFromFireStorage(imageName)
                    val mark = MapMark(
                        name = imageName, url = imageUrl, date = AppDateUtils.formatDate(
                            Date(), AppDateUtils.longPhotoDatePattern
                        ), imageLatitude = imgLat, imageLongitude = imgLng
                    )
                    mapMarkRepository.uploadMarkToFirebase(mark)
                    mapMarkList.add(mark)
                    dataList.postValue(mapMarkList)
                    Log.d("ViewModelLog", "image uploaded")
                }
            } catch (e: Exception) {
                Log.d("ViewModelLog", e.message.toString())
            }
        }
    }

    fun getAllMarksFromFirebase() {
        viewModelScope.launch {
            mapMarkList.clear()
            val querySnapshot =
                categoryLiveDataList.value?.let {
                    mapMarkRepository.getAllMarksFromFirebase(
                        MAP_MARK_FIELD_CATEGORY,
                        it
                    )
                }
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    document.toObject<MapMark>()?.let {
                        mapMarkList.add(it)
                    }
                }
            }
            this@MainViewModel.dataList.postValue(mapMarkList)
            Log.d("ViewModelLog", "download")
        }
    }

    fun getMarksFromLocalDB() {
        viewModelScope.launch {
            mapMarkList.clear()
            categoryLiveDataList.value?.let {
                for (mark in mapMarkRepository.getAllMarksFromDB(it)) {
                    mapMarkList.add(mark)
                }
            }
            dataList.postValue(mapMarkList)
        }
    }

    fun searchMapMarks(query: String) {
        viewModelScope.launch {
            mapMarkList.clear()
            val querySnapshot =
                categoryLiveDataList.value?.let {
                    mapMarkRepository.searchMarksInFirebase(
                        MAP_MARK_FIELD_CATEGORY,
                        it,
                        query
                    )
                }
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    document.toObject<MapMark>()?.let {
                        mapMarkList.add(it)
                    }
                }
            }
            this@MainViewModel.dataList.postValue(mapMarkList)
            Log.d("ViewModelLog", "search")
        }
    }

    fun syncLocalDB(activity: MainActivity) {
        var dataFromLocalDB: List<MapMark>
        CoroutineScope(Dispatchers.IO).launch {
            dataFromLocalDB = mapMarkRepository.getAllMarksFromDB(categoryList)
            Log.d("mainLog", dataFromLocalDB.toString())
            if (dataFromLocalDB.isEmpty()) {
                try {
                    val querySnapshot = mapMarkRepository.getAllMarksFromFirebase(
                        MAP_MARK_FIELD_CATEGORY,
                        categoryList
                    )
                    for (document in querySnapshot.documents) {
                        document.toObject<MapMark>()?.let {
                            val url = URL(it.url)
                            val bitmap =
                                BitmapFactory.decodeStream(url.openConnection().getInputStream())
                            val savedImageURI = MediaStore.Images.Media.insertImage(
                                activity.contentResolver,
                                bitmap,
                                it.name,
                                it.description
                            )
                            mapMarkRepository.addMarkToDB(it.also { it.url = savedImageURI })
                        }
                    }
                } catch (e: Exception) {
                    Log.d("mainLog", e.message.toString())
                }
            }
        }
    }

//    private fun syncLocalDatabaseWithFirebase() {
//        val marksFromFirebase: MutableList<MapMark> = mutableListOf()
//        val marksFromLocalDB: MutableList<MapMark> = mutableListOf()
//        viewModelScope.launch {
//            val firebaseObs = Observable.fromIterable(
//                mapMarkRepository.getAllMarksFromFirebase(
//                    MAP_MARK_FIELD_CATEGORY,
//                    categoryList
//                ).documents
//            )
//                .map {
//                    it.toObject<MapMark>()
//                }.map {
//                    Log.d("ViewModelLog", "from FireBase $it")
//                }.subscribe()
//            val localObs = Observable.fromIterable(
//                mapMarkRepository.getAllMarksFromDB(categoryList)
//            ).map {
//                Log.d("ViewModelLog", "from DB $it")
//            }.subscribe()
////            Observable.merge(firebaseObs, localObs).map {
////
////            }
//
//        }
//    }
}