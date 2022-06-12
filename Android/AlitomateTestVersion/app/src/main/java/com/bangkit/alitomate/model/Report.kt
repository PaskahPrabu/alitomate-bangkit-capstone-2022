package com.bangkit.alitomate.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Report(
    val name: String,
    val phoneNumber: String,
    val detailLoc: String?,
    val detailReport: String?,
    val imageUrl: String?,
    val lat: String,
    val lon: String,
    @ServerTimestamp
    val timestamp: Date?
)
