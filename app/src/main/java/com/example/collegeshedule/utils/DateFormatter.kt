package com.example.collegeshedule.utils

import com.example.collegeshedule.data.dto.LessonGroupPart
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateString: String, weekday: String): String {
    return try {
        // Убираем время из ISO datetime если есть
        val dateOnly = if (dateString.contains("T")) {
            dateString.split("T")[0]
        } else {
            dateString
        }
        
        val date = LocalDate.parse(dateOnly)
        val dayOfMonth = date.dayOfMonth
        val month = when (date.monthValue) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""
        }
        val year = date.year
        "$dayOfMonth $month $year г. - $weekday"
    } catch (e: Exception) {
        "$dateString - $weekday"
    }
}

fun formatSubgroup(subgroup: LessonGroupPart): String {
    return when (subgroup) {
        LessonGroupPart.SUB1 -> "1 подгруппа"
        LessonGroupPart.SUB2 -> "2 подгруппа"
        LessonGroupPart.FULL -> ""
    }
}