package com.example.photomap.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomap.firebase.FirebaseInstance.Companion.fireBaseImageStorage
import com.example.photomap.firebase.FirebaseInstance.Companion.fireStoreDB
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.STORAGE_PATH
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MainViewModel : ViewModel() {

    val dataList: MutableLiveData<List<MapMark>> = MutableLiveData()

    init {
        downloadAllData()

    }

    fun uploadMapMark(imageFile: Uri, imageName: String) {
        viewModelScope.launch {
            try {
                imageName.let {
                    fireBaseImageStorage.child("$STORAGE_PATH$imageName").putFile(imageFile).await()
                    val imageUrl = fireBaseImageStorage.child("$STORAGE_PATH$imageName")
                        .downloadUrl.await().toString()
                    fireStoreDB.add(MapMark(name = imageName, url = imageUrl))
                    Log.d("ViewModelLog", "image uploaded")
                    downloadAllData()
                }
            } catch (e: Exception) {
                Log.d("ViewModelLog", e.message.toString())
            }
        }
    }

    fun downloadAllData() {
        val dataList = mutableListOf<MapMark>()
        viewModelScope.launch {
            val querySnapshot = fireStoreDB.get().await()
            var mapMark: MapMark
            for (document in querySnapshot.documents) {
                    mapMark = document.toObject<MapMark>()!!
                    dataList.add(mapMark)
            }
            Log.d("ViewModelLog", "download" )
            this@MainViewModel.dataList.postValue(dataList)
        }
    }


}