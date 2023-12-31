package com.opasichnyi.beautify.domain.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateMapper {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    fun mapStringToDate(dateTimeString: String): Date =
        sdf.parse(dateTimeString) as Date

    fun mapDateToString(date: Date): String =
        sdf.format(date)
}