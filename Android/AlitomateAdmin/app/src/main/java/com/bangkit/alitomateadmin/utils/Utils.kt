package com.bangkit.alitomateadmin.utils

import java.text.SimpleDateFormat
import java.util.*

fun getDate(time:Long):String {
    val date: Date = Date(time*1000L); // *1000 is to convert seconds to milliseconds
    val sdf: SimpleDateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.US); // the format of your date
    sdf.setTimeZone(TimeZone.getTimeZone("UTC+7"));
    return sdf.format(date);
}