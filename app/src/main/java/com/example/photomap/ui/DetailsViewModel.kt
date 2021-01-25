package com.example.photomap.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomap.model.MapMark
import com.example.photomap.repository.MapMarkRepository
import kotlinx.coroutines.launch

class DetailsViewModel(private val mapMarkRepository: MapMarkRepository) : ViewModel() {

    fun updateMapMarkDescription(mapMark: MapMark) {
        val mapOfMapMark = mutableMapOf<String, Any>()
        mapOfMapMark.apply {
            put("description", mapMark.description)
        }
        viewModelScope.launch {
            val mapMarkQuery = mapMarkRepository.getMapMark(mapMark)
            if (mapMarkQuery.documents.isNotEmpty()) {
                try {
                    mapMarkRepository.updateMapMark(mapMarkQuery, mapOfMapMark)
                } catch (e: Exception) {
                    Log.d("myLog", e.message.toString())
                }
            } else Log.d("myLog", "Collection is empty")
        }
    }
}