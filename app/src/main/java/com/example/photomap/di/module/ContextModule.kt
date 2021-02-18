package com.example.photomap.di.module

import android.content.Context
import com.example.photomap.di.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ContextModule(private val context: Context) {

    @Named("application_context")
    @ApplicationScope
    @Provides
    fun context(): Context{
        return this.context.applicationContext
    }
}