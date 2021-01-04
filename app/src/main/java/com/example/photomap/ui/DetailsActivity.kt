package com.example.photomap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.photomap.R

class DetailsActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.TransparentTheme)
        setContentView(R.layout.activity_details)

        navController = Navigation.findNavController(this, R.id.detailsNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}