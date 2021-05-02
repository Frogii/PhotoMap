package com.example.photomap

import android.app.Application
import com.example.photomap.di.component.DaggerAppComponent
import com.example.photomap.di.module.ContextModule

class App : Application() {

    companion object {
        lateinit var daggerAppComponent: DaggerAppComponent
        lateinit var applicationRes: Application

        fun getApplication(): Application {
            return applicationRes
        }
    }

    override fun onCreate() {
        super.onCreate()
        applicationRes = this
        daggerAppComponent = DaggerAppComponent.builder().contextModule(ContextModule(this))
            .build() as DaggerAppComponent
    }
}