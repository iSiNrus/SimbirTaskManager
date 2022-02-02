package ru.barsik.simbirtaskmanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task(

    @PrimaryKey
    var id: Int = 0,

    var date_finish: String = "Unknown",
    var date_start: String = "Unknown",
    var description: String = "Unknown",
    var name: String = "Unknown"

) : RealmObject() {

    fun getCalendarStart(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date_start.toLong()
        return cal
    }

}