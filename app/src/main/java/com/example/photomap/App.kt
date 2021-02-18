package com.example.photomap

import android.app.Application
import com.example.photomap.di.component.DaggerAppComponent
import com.example.photomap.di.module.ContextModule

class App : Application() {

    companion object {
        lateinit var daggerAppComponent: DaggerAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        daggerAppComponent = DaggerAppComponent.builder().contextModule(ContextModule(this)).build() as DaggerAppComponent
    }
}