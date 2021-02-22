package com.example.photomap.di.module

import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.ui.MainViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class MainViewModelProviderFactoryModule {

    @Provides
    fun provideViewModelFactory(repository: MapMarkRepository): MainViewModelProviderFactory {
        return MainViewModelProviderFactory(repository)
    }
}