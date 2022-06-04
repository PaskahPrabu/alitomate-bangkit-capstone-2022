package com.bangkit.alitomate.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bangkit.alitomate.R
import com.bangkit.alitomate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashOnScreen = true
        val delay = 1000L

        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{keepSplashOnScreen}
        Handler(Looper.getMainLooper()).postDelayed({keepSplashOnScreen = false}, delay)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction() {
        binding.reportButton.setOnClickListener {
            val intent = Intent(this, ReportingActivity::class.java)
            startActivity(intent)
        }

        binding.emergencyButton.setOnClickListener {
            val emergencyNumber = "112"
            val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$emergencyNumber"))
            startActivity(dialPhoneIntent)
        }
    }
}