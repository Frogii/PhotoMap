package com.example.photomap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photomap.repository.MapMarkRepository

class MainViewModelProviderFactory(val mapMarkRepository: MapMarkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mapMarkRepository) as T
    }
}