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
import com.example.photomap.util.ResProvider
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class MainViewModel(
    private val mapMarkRepository: MapMarkRepository,
    private val resourcesProvider: ResProvider
) : ViewModel() {

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
        followButtonLiveDataState.value = true
        categoryLiveDataList.value = categoryList
        checkBoxLiveDataStateMap.postValue(checkBoxStateMap)
    }

    fun uploadMapMark(imageFile: Uri, imageName: String, imgLat: Double, imgLng: Double) {
        viewModelScope.launch {
            try {
                mapMarkList.add(mapMarkRepository.uploadMark(imageFile, imageName, imgLat, imgLng))
                dataList.postValue(mapMarkList)
                Log.d("ViewModelLog", "image uploaded")
            } catch (e: Exception) {
                Log.d("ViewModelLog", e.message.toString())
            }
        }
    }

    fun deleteMapMark(mapMark: MapMark) {
        viewModelScope.launch {
            try {
                mapMarkRepository.deleteMark(mapMark)
            } catch (e: Exception) {
                Log.d("ViewModelLog", e.message.toString())
            }
            mapMarkList.remove(mapMark)
            dataList.postValue(mapMarkList)
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

    suspend fun gatDataFromLocalDB(): List<MapMark> {
        return mapMarkRepository.getAllMarksFromDB(categoryList)
    }

    fun syncLocalDB() {
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
                                resourcesProvider.getContentResolver(),
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
}