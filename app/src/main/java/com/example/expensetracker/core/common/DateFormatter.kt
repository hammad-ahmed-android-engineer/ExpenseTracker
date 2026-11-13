package com.example.expensetracker.core.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.formatDate(pattern: String = "MMM dd, yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.formatMonthYear(): String = this.formatDate("MMMM yyyy")

fun Long.formatRelativeDate(): String {
    val targetCal = Calendar.getInstance().apply { timeInMillis = this@formatRelativeDate }
    val nowCal = Calendar.getInstance()

    return when {
        targetCal.isSameCalendarDay(nowCal) -> "Today"
        targetCal.isYesterdayOf(nowCal) -> "Yesterday"
        else -> this.formatDate("MMM dd, yyyy")
    }
}

fun Long.toIsoDate(): String = this.formatDate("yyyy-MM-dd")

fun Int.startOfMonthMillis(year: Int): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, this@startOfMonthMillis)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}

fun Int.endOfMonthMillis(year: Int): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, this@endOfMonthMillis)
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return cal.timeInMillis
}

fun Long.startOfDayMillis(): Long {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@startOfDayMillis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}

fun Long.endOfDayMillis(): Long {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@endOfDayMillis
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return cal.timeInMillis
}

private fun Calendar.isSameCalendarDay(other: Calendar): Boolean =
    get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
        get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

private fun Calendar.isYesterdayOf(now: Calendar): Boolean {
    val yesterday = (now.clone() as Calendar).apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    return isSameCalendarDay(yesterday)
}
