package com.example.photomap.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.photomap.R
import com.example.photomap.repository.MapMarkRepository
import com.example.photomap.util.AppConnectionUtils
import com.example.photomap.util.AppPermissionUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeLineViewModelProviderFactory =
            MainViewModelProviderFactory(MapMarkRepository.invoke(this))
        mainViewModel =
            ViewModelProvider(this, timeLineViewModelProviderFactory).get(MainViewModel::class.java)

        navController = Navigation.findNavController(this, R.id.mainNavHostFragment)
        bottomNavView.setupWithNavController(mainNavHostFragment.findNavController())
        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.categoriesFragment || destination.id == R.id.detailsFragment ||
                destination.id == R.id.photoFragment
            ) {
                bottomNavView.visibility = View.GONE
            } else {
                bottomNavView.visibility = View.VISIBLE
            }
        }

        if (AppConnectionUtils.checkConnection(this)){
            if(AppPermissionUtils.checkWriteStoragePermission(this))
                mainViewModel.syncLocalDB(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (AppConnectionUtils.checkConnection(this)) {
            mainViewModel.getAllMarksFromFirebase()
            Log.d("myLog", "from NET")
        } else {
            Log.d("myLog", "from DB")
            if (AppPermissionUtils.checkReadStoragePermission(this@MainActivity))
                mainViewModel.getMarksFromLocalDB()
        }
    }
}