package com.bangkit.alitomateadmin.data

import com.google.firebase.Timestamp
import java.util.*

data class Report(
    val name: String = "",
    val phoneNumber: String = "",
    val detailLoc: String = "",
    val detailReport: String = "",
    val lat: String = "",
    val lon: String = "",
    val imageUrl: String = "",
    val timestamp: Timestamp? = null
    )
