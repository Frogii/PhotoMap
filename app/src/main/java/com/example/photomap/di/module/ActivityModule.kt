package com.example.photomap.di.module

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.photomap.di.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ActivityModule(private val context: Activity) {

    @Named("activity_context")
    @ApplicationScope
    @Provides
    fun context(): Context {
        return this.context
    }
}