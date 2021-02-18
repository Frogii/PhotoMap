package com.example.photomap.di.component

import com.example.photomap.di.ApplicationScope
import com.example.photomap.di.module.DetailsViewModelModule
import com.example.photomap.di.module.MainViewModelModule
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.ui.MainActivity
import dagger.Component

@ApplicationScope
@Component(modules = [MainViewModelModule::class, DetailsViewModelModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: DetailsActivity)
}