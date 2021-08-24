package com.softcare.raphnote.db

import android.text.format.DateUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class Schema {
          val tag="RaphNote"
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

class TaskManager private constructor() {
    val workQueue: BlockingQueue<Runnable>
    private val threadPoolExecutor: ThreadPoolExecutor

    companion object {
        var instance: TaskManager? = null
            private set
        private const val CORE_POL_SIZE = 5
        private const val KEEP_ALIVE_TIME = 50L
        private const val MAX_POL_SIZE = 10

        init {
            instance = TaskManager()
        }
    }

    fun runTask(runnable: Runnable?): TaskManager? {
        threadPoolExecutor.execute(runnable)
        return instance
    }

    fun toast(activity: AppCompatActivity, msg: String?) {
        activity.runOnUiThread {
            Toast.makeText(activity.applicationContext, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun snackBar(activity: AppCompatActivity, msg: String?, view: View) {
        activity.runOnUiThread { Snackbar.make(view, msg!!, Snackbar.LENGTH_LONG).show() }
    }

    init {
        workQueue = LinkedBlockingDeque<Runnable>()
        threadPoolExecutor = ThreadPoolExecutor(
            CORE_POL_SIZE, MAX_POL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS, workQueue
        )
    }
}