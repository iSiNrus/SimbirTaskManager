package ru.barsik.simbirtaskmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.barsik.simbirtaskmanager.databinding.ActivityMainBinding
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
    }
}