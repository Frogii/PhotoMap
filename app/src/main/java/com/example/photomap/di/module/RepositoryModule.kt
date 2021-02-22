package com.example.photomap.di.module

import com.example.photomap.db.MapMarkLocalDatabase
import com.example.photomap.di.ApplicationScope
import com.example.photomap.repository.MapMarkRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RoomModule::class])
class RepositoryModule {

    @ApplicationScope
    @Provides
    fun provideRepository(room: MapMarkLocalDatabase): MapMarkRepository {
        return MapMarkRepository(room)
    }
}