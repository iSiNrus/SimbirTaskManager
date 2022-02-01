package ru.barsik.simbirtaskmanager.model

import java.util.*

data class Task(
    val date_finish: String,
    val date_start: String,
    val description: String,
    val id: Int,
    val name: String
){

    fun getCalendarStart(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date_start.toLong()
        return cal
    }

}