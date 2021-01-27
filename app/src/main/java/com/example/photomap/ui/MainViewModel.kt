package com.example.photomap.ui

import android.net.Uri
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
import kotlinx.coroutines.launch
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

    init {
        categoryLiveDataList.value = categoryList
        checkBoxLiveDataStateMap.postValue(checkBoxStateMap)
        getAllMapMarks()
    }

    fun uploadMapMark(imageFile: Uri, imageName: String) {
        viewModelScope.launch {
            try {
                imageName.let {
                    mapMarkRepository.uploadPhoto(imageFile, imageName)
                    val imageUrl = mapMarkRepository.getImageUrl(imageName)
                    val mark = MapMark(
                        name = imageName, url = imageUrl, date = AppDateUtils.formatDate(
                            Date(), AppDateUtils.longPhotoDatePattern
                        )
                    )
                    mapMarkRepository.uploadMapMark(mark)
                    mapMarkList.add(mark)
                    dataList.postValue(mapMarkList)
                    Log.d("ViewModelLog", "image uploaded")
                }
            } catch (e: Exception) {
                Log.d("ViewModelLog", e.message.toString())
            }
        }
    }

    fun getAllMapMarks() {
        viewModelScope.launch {
            mapMarkList.clear()
            val querySnapshot =
                categoryLiveDataList.value?.let { mapMarkRepository.getAllMapMarks(MAP_MARK_FIELD_CATEGORY, it) }
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

    fun searchMapMarks(query: String) {
        viewModelScope.launch {
            mapMarkList.clear()
            val querySnapshot =
                categoryLiveDataList.value?.let {
                    mapMarkRepository.searchMapMarks(
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
}