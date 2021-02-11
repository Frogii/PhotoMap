package com.example.photomap.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photomap.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            if (user != null) {
                val mainActivityIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            } else {
                val logInIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(logInIntent)
                finish()
            }
        }
    }
}