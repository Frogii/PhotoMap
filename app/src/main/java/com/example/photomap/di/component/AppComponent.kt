package com.example.photomap.di.component

import com.example.photomap.di.ApplicationScope
import com.example.photomap.di.module.DetailsViewModelProviderFactoryModule
import com.example.photomap.di.module.MainViewModelProviderFactoryModule
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.ui.MainActivity
import dagger.Component

@ApplicationScope
@Component(modules = [MainViewModelProviderFactoryModule::class, DetailsViewModelProviderFactoryModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(detailsActivity: DetailsActivity)
}