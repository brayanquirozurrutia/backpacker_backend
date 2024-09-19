package com.example.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun convertDateFormatIfNecessary(date: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return try {
        val parsedDate = LocalDate.parse(date, inputFormatter)
        parsedDate.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        date
    }
}
