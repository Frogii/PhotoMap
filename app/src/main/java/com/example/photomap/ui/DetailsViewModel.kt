package com.example.photomap.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomap.model.MapMark
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.Constants.CATEGORY_MAP_KEY
import com.example.photomap.util.Constants.DESCRIPTION_MAP_KEY
import kotlinx.coroutines.launch


class DetailsViewModel(private val mapMarkRepository: MapMarkRepository) : ViewModel() {

    private val categoryState = MutableLiveData<String>()
    private val descriptionState = MutableLiveData<String>()

    fun saveCategoryState(content: String) {
        categoryState.postValue(content)
    }

    fun saveDescriptionState(content: String) {
        descriptionState.postValue(content)
    }

    fun getCategoryState(): MutableLiveData<String> {
        return categoryState
    }

    fun getDescriptionState(): MutableLiveData<String> {
        return descriptionState
    }

    fun updateMapMarkDetails(mapMark: MapMark) {
        val mapOfMapMark = mutableMapOf<String, Any>()
        mapOfMapMark.apply {
            put(DESCRIPTION_MAP_KEY, mapMark.description)
            put(CATEGORY_MAP_KEY, mapMark.category)
        }
        viewModelScope.launch {
            mapMarkRepository.addMarkToDB(mapMark)
            val mapMarkQuery = mapMarkRepository.getMarkFromFirebase(mapMark)
            if (mapMarkQuery.documents.isNotEmpty()) {
                try {
                    mapMarkRepository.updateMapMarkInFirebase(mapMarkQuery, mapOfMapMark)
                } catch (e: Exception) {
                    Log.d("myLog", e.message.toString())
                }
            } else Log.d("myLog", "Collection is empty")
        }
    }
}