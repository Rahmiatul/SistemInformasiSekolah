package com.rahmi.sisteminformasisekolah

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Set a 3-second delay before transitioning to the next screen
        Handler(mainLooper).postDelayed({
            // Start the next activity (replace MainActivity::class.java with your next activity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Close the splash screen activity
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}