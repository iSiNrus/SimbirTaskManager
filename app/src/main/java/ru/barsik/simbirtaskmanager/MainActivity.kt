package ru.barsik.simbirtaskmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirtaskmanager.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.recyclerview.layoutManager = LinearLayoutManager(this)

        binder.calendarView.setOnDayClickListener {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            val date: String = sdf.format(it.calendar.time)
            binder.tvChoosenDate.text = date
        }
        val tasks = getTasks()
        for(t in tasks)
            Log.d(TAG, t.name)
    }


    private fun getTasks(): List<Task> {
        val br = BufferedReader(InputStreamReader(assets.open("tasks.json")))
        var s: String? = ""
        val jsonString = StringBuilder()

        while (s != null) {
            s = br.readLine()
            jsonString.append(s ?: "")
        }
        br.close()
        val gson = Gson()
        val taskType = object : TypeToken<List<Task>>() {}.type

        return gson.fromJson(jsonString.toString(), taskType) as List<Task>
    }
}