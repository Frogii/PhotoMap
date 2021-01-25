package com.example.photomap.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomap.model.MapMark
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.AppDateUtils
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val mapMarkRepository: MapMarkRepository) : ViewModel() {

    private val mapMarkList = mutableListOf<MapMark>()
    val dataList: MutableLiveData<List<MapMark>> = MutableLiveData()

    init {
        getAllMapMarks()
    }

    fun uploadMapMark(imageFile: Uri, imageName: String) {
        viewModelScope.launch {
            try {
                imageName.let {
                    mapMarkRepository.uploadPhoto(imageFile, imageName)
                    val imageUrl = mapMarkRepository.getImageUrl(imageName)
                    val mark = MapMark(name = imageName, url = imageUrl, date = AppDateUtils.formatDate(
                        Date(), AppDateUtils.longPhotoDatePattern
                    ))
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
            val querySnapshot = mapMarkRepository.getAllMapMarks()
            for (document in querySnapshot.documents) {
                document.toObject<MapMark>()?.let {
                    mapMarkList.add(it)
                }
            }
            this@MainViewModel.dataList.postValue(mapMarkList)
            Log.d("ViewModelLog", "download")
        }
    }
}