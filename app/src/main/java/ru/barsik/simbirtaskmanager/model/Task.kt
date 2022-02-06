package ru.barsik.simbirtaskmanager.model

import org.bson.types.ObjectId
import java.io.Serializable
import java.util.*

data class Task(
    var id: String = ObjectId.get().toHexString(),
    var date_finish: String = "Unknown",
    var date_start: String = "Unknown",
    var description: String = "Unknown",
    var name: String = "Unknown"
): Serializable {

    fun getCalendarStart(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date_start.toLong()
        return cal
    }
    fun getCalendarFinish(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date_finish.toLong()
        return cal
    }

}