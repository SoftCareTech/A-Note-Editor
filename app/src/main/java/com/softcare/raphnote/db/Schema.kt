package com.softcare.raphnote.db

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


class Schema {

    object Note {
        const val ID = "id" // creation time
        const val TIME = "time" //creation time
        const val NAME = "name"
        const val TAG = "RaphNote"
    }

    private fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }

    fun getTime(aLong: Long): String? {
        var formatter: SimpleDateFormat? = null
        val date = Date(aLong)
        if (DateUtils.isToday(aLong)) formatter =
            SimpleDateFormat("hh:mm:ss a") else if (isYesterday(date)) return "Yesterday" else formatter =
            SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(date)
    }

    var DATE_PATTERN = "dd/MM/yyyy HH:mm"
    var DATE_PATTERN_NON = "ddMMyyyyHHmm"

    fun getDateAndTime(aLong: Long): String? {
        val formatter = SimpleDateFormat(DATE_PATTERN)
        val date = Date(aLong)
        return formatter.format(date)
    }

    fun getLongDateTime(s: String): Long {
        try {
            val sdf = SimpleDateFormat(DATE_PATTERN)
            val d: Date = sdf.parse(s)
            return d.getTime()
        } catch (e: Exception) {
        }
        return 0L
    }

    fun getDateFromString(s: String): Date {
        try {
            val sdf = SimpleDateFormat(DATE_PATTERN)
            val d: Date = sdf.parse(s)
           // val l: Long = d.getTime()
            return d
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Date(0)
    }

    fun getDateFromLong(aLong: Long): Date? {
       // val formatter = SimpleDateFormat(DATE_PATTERN)
        return Date(aLong)
    }


}