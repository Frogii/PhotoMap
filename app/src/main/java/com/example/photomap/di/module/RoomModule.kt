package com.example.photomap.di.module

import android.content.Context
import com.example.photomap.db.MapMarkLocalDatabase
import com.example.photomap.di.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class RoomModule {

    @ApplicationScope
    @Provides
    fun provideRoom(@Named("application_context") context: Context): MapMarkLocalDatabase {
        return MapMarkLocalDatabase.createDatabase(context)
    }
}