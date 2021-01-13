package com.example.photomap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimeLineViewModelProviderFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }

}