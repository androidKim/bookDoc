package com.midasgo.bookdoc.com.midasgo.bookdoc.util

import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*


class Util {

    companion object{

        //
        fun getDateFromDatePicker(datePicker: DatePicker): Date {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            return calendar.getTime()
        }

        //
        fun getDateFromTimestamp(timestamp: Long, format:String): String? {
            try {
                val sdf = SimpleDateFormat(format)
                val netDate = Date(timestamp)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }

    }
}