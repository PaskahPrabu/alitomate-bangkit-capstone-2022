package com.bangkit.alitomate.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.alitomate.R
import com.bangkit.alitomate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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