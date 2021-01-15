package com.example.photomap.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.photomap.R
import com.example.photomap.util.Constants.ITEM_FROM_RECYCLER

class DetailsActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.TransparentTheme)
        setContentView(R.layout.activity_details)

        navController = Navigation.findNavController(this, R.id.detailsNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val item = intent.getSerializableExtra(ITEM_FROM_RECYCLER)
        Log.d("myLog", item.toString())
    }
}