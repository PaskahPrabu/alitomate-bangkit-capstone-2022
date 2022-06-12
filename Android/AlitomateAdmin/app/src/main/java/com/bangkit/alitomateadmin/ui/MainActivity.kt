package com.bangkit.alitomateadmin.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.alitomateadmin.data.Report
import com.bangkit.alitomateadmin.databinding.ActivityMainBinding
import com.bangkit.alitomateadmin.ui.adapter.ReportAdapter
import com.google.firebase.firestore.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var list: List<Report> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val reportAdapter: ReportAdapter = ReportAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashOnScreen = true
        val delay = 1000L

        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        getReport()
        showLoading(false)
        showRecyclerList()

        binding.fabMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getReport() {
        showLoading(true)
        db.collection("report")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    list = it.result!!.toObjects(Report::class.java)
                    reportAdapter.listItem = list
                    reportAdapter.notifyDataSetChanged()
                    showLoading(false)
                } else {
                    Log.e("main", "Data tidak dapat diambil")
                    Toast.makeText(this, "Data tidak dapat diambil", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        binding.reportRv.layoutManager = LinearLayoutManager(this)
        binding.reportRv.adapter = reportAdapter
        reportAdapter.setOnItemClickCallback(object : ReportAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Report) {
                var latitude = data.lat
                var longitude = data.lon
                val mapsUri = "https://www.google.com/maps/search/?api=1&query=$latitude%2C$longitude"
                val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUri))
                mapsIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapsIntent)
            }
        })
    }
}