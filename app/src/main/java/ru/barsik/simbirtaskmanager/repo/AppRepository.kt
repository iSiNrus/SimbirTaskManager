package ru.barsik.simbirtaskmanager.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirtaskmanager.model.Task
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class AppRepository(private val ctx: Context) {

    fun getTasks(date: Calendar): List<Task> {
        val br = BufferedReader(InputStreamReader(ctx.assets.open("tasks.json")))
        var s: String? = ""
        val jsonString = StringBuilder()

        while (s != null) {
            s = br.readLine()
            jsonString.append(s ?: "")
        }
        br.close()
        val gson = Gson()
        val taskType = object : TypeToken<List<Task>>() {}.type

        val tasks = gson.fromJson(jsonString.toString(), taskType) as MutableList<Task>

        tasks.removeIf {
            !(it.getCalendarStart().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    it.getCalendarStart().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR))
        }

        return tasks
    }

}