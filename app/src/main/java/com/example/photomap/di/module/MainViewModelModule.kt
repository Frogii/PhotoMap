package com.example.photomap.di.module

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.photomap.ui.MainViewModel
import com.example.photomap.ui.MainViewModelProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityModule::class, MainViewModelProviderFactoryModule::class])
class MainViewModelModule {

    @Provides
    fun provideMainViewModel(
        @Named("activity_context") context: Context,
        factory: MainViewModelProviderFactory
    ): MainViewModel {
        return ViewModelProvider(context as AppCompatActivity, factory).get(MainViewModel::class.java)
    }
}