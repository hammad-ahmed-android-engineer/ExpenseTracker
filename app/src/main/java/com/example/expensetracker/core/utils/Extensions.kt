package com.example.expensetracker.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun getTimestamp(date: String): Long {
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        .parse(date)!!.time
}

fun String.toTimestampMillis(): Long = getTimestamp(this)
