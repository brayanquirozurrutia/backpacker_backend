package com.example.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun convertDateFormat(dateString: String): String {
    return try {
        val formatterInput = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatterInput)
        date.format(formatterOutput)
    } catch (e: DateTimeParseException) {
        throw e
    }
}
