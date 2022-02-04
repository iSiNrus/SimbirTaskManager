package ru.barsik.simbirtaskmanager.presentation

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.barsik.simbirtaskmanager.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var binder: ActivityAddBinding
    private val dateTimeStart = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.ivBack.setOnClickListener{
            onBackPressed()
        }

        binder.btnStartDate.setOnClickListener {
            DatePickerDialog(
                this, d,
                dateTimeStart.get(Calendar.YEAR),
                dateTimeStart.get(Calendar.MONTH),
                dateTimeStart.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    var d = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateTimeStart.set(Calendar.YEAR, year)
            dateTimeStart.set(Calendar.MONTH, monthOfYear)
            dateTimeStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }

    var t = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateTimeStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateTimeStart.set(Calendar.MINUTE, minute)
        setInitialDateTime()
    }

    private fun setInitialDateTime(){
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        val date: String = sdf.format(dateTimeStart.time)
        binder.btnStartDate.text = date
        //TODO доделать
    }
}