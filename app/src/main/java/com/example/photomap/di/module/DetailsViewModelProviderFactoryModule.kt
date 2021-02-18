package com.example.photomap.di.module

import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.ui.DetailsViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class DetailsViewModelProviderFactoryModule {

    @Provides
    fun provideViewModelFactory(repository: MapMarkRepository): DetailsViewModelProviderFactory {
        return DetailsViewModelProviderFactory(repository)
    }
}