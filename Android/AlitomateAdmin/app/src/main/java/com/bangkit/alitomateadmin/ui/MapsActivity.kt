package com.bangkit.alitomateadmin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bangkit.alitomateadmin.R
import com.bangkit.alitomateadmin.data.Report
import com.bangkit.alitomateadmin.databinding.ActivityMapsBinding
import com.bangkit.alitomateadmin.utils.getDate

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var list: List<Report> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db.collection("report")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    list = it.result!!.toObjects(Report::class.java)
                    list.forEachIndexed { _, report ->
                        var latitude = report.lat.toDouble()
                        var longitude = report.lon.toDouble()

                        val location = LatLng(
                            latitude,
                            longitude
                        )
                        mMap
                            .addMarker(MarkerOptions()
                                .position(location)
                                .title(report.timestamp?.let { getDate(it.seconds) })
                            )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                    }
                } else {
                    Log.e("main", "Data tidak dapat diambil")
                    Toast.makeText(this, "Data tidak dapat diambil", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
    }
}