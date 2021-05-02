package com.example.photomap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photomap.App
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.ResProvider

class MainViewModelProviderFactory(val mapMarkRepository: MapMarkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mapMarkRepository, ResProvider(App.getApplication())) as T
    }
}