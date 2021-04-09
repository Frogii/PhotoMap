package com.example.photomap.ui

import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.photomap.App
import com.example.photomap.R
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.Constants.MAP_MARK_ITEM
import com.example.photomap.util.injectViewModel
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var detailsViewModelProviderFactory: DetailsViewModelProviderFactory
    lateinit var navController: NavController
    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.TransparentTheme)
        App.daggerAppComponent.inject(this)
        setContentView(R.layout.activity_details)
        window.sharedElementReturnTransition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds)

        detailsViewModel = injectViewModel(detailsViewModelProviderFactory)
        val mapMark = intent.getSerializableExtra(MAP_MARK_ITEM)
        navController = Navigation.findNavController(this, R.id.detailsNavHostFragment)
        //WOW it works... sending data to fragment from activity by using navigation
        navController.setGraph(
            R.navigation.details_nav_graph,
            Bundle().also { it.putSerializable(MAP_MARK_ITEM, mapMark) })
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}