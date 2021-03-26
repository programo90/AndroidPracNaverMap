package com.programo90.promaps

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.*

class CurrTimer {
    companion object {
        fun getCurrDate():String {
            var currdate = Calendar.getInstance().time
            return SimpleDateFormat("yyyyMMddHH", Locale.KOREA).format(currdate)
        }
    }
}