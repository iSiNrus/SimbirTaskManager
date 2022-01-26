package ru.barsik.simbirtaskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import ru.barsik.simbirtaskmanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val binder by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binder.recyclerview.layoutManager = LinearLayoutManager(this)


        binder.calendarView.setOnDateChangeListener { calendarView: CalendarView, i: Int, i2: Int, i3: Int ->
            var date = Date(calendarView.date)
            Log.d(TAG, "onCreate: $date")
        }
    }
}