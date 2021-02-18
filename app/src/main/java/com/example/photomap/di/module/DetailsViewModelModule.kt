package com.example.photomap.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.photomap.ui.DetailsViewModel
import com.example.photomap.ui.DetailsViewModelProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityModule::class, DetailsViewModelProviderFactoryModule::class])
class DetailsViewModelModule {

    @Provides
    fun provideDetailsViewModel(
        @Named("activity_context") context: Context,
        factory: DetailsViewModelProviderFactory
    ): DetailsViewModel {
        return ViewModelProvider(context as AppCompatActivity, factory).get(DetailsViewModel::class.java)
    }
}