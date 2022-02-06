package ru.barsik.simbirtaskmanager.presentation

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.barsik.simbirtaskmanager.databinding.ActivityAddBinding
import ru.barsik.simbirtaskmanager.model.Task
import ru.barsik.simbirtaskmanager.repo.AppRepository
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private val TAG = "AddActivity"
    private lateinit var binder: ActivityAddBinding
    private var dateTimeStart = Calendar.getInstance()
    private var dateTimeFinish = Calendar.getInstance()
    private var isEdit = false
    private var mTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binder.root)

        if(intent.getSerializableExtra(MainActivity.EXTRA_TASK) != null) {
            isEdit = true
            mTask = intent.getSerializableExtra(MainActivity.EXTRA_TASK) as Task
            Log.d(TAG, "onCreate: ${mTask?.toString()}")
            dateTimeStart = mTask?.getCalendarStart()
            dateTimeFinish = mTask?.getCalendarFinish()

            binder.tfName.editText?.setText(mTask?.name)
            binder.tfDescript.editText?.setText(mTask?.description)

            binder.toolbarTitle.title = "Редактирование"
            binder.ivDelete.visibility = View.VISIBLE
        }

        setInitialDateTime()

        binder.ivBack.setOnClickListener{
            onBackPressed()
        }

        binder.ivDelete.setOnClickListener {
            val repo = AppRepository(this)
            repo.deleteTask(mTask!!)
            onBackPressed()
        }

        binder.btnStartDate.setOnClickListener {
            DatePickerDialog(
                this, ds,
                dateTimeStart.get(Calendar.YEAR),
                dateTimeStart.get(Calendar.MONTH),
                dateTimeStart.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binder.btnFinishDate.setOnClickListener {
            DatePickerDialog(
                this, df,
                dateTimeFinish.get(Calendar.YEAR),
                dateTimeFinish.get(Calendar.MONTH),
                dateTimeFinish.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binder.btnStartTime.setOnClickListener {
            TimePickerDialog(
                this, ts,
                dateTimeStart.get(Calendar.HOUR_OF_DAY),
                dateTimeStart.get(Calendar.MINUTE),
                true
            ).show()
        }

        binder.btnFinishTime.setOnClickListener {
            TimePickerDialog(
                this, tf,
                dateTimeFinish.get(Calendar.HOUR_OF_DAY),
                dateTimeFinish.get(Calendar.MINUTE),
                true
            ).show()
        }

        binder.btnSave.setOnClickListener {
            val repo = AppRepository(this)
            val dateFinish = dateTimeFinish.timeInMillis.toString()
            val dateStart = dateTimeStart.timeInMillis.toString()
            val name = binder.tfName.editText?.text.toString().trim()
            val descr = binder.tfDescript.editText?.text.toString().trim()

            if(name == "" || descr == "" || (dateFinish.toLong() - dateStart.toLong()) < 0){
                Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show()
            } else {
                if(isEdit)
                {
                    mTask?.name = name
                    mTask?.description = descr
                    mTask?.date_start = dateStart
                    mTask?.date_finish = dateFinish
                    Log.d(TAG, "onCreate: ${mTask.toString()}")
                    repo.saveTask(mTask!!).subscribe({
                        this.runOnUiThread {
                            Toast.makeText(this, "Успешно сохранено", Toast.LENGTH_SHORT).show()
                        }
                    }, {
                        this.runOnUiThread {
                            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                else{
                    repo.saveTask(
                        Task(
                            name = name,
                            date_start = dateStart,
                            date_finish = dateFinish,
                            description = descr
                        )
                    ).subscribe({
                        this.runOnUiThread {
                            Toast.makeText(this, "Успешно сохранено", Toast.LENGTH_SHORT).show()
                        }
                    }, {
                        this.runOnUiThread {
                            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private var ds = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateTimeStart.set(Calendar.YEAR, year)
            dateTimeStart.set(Calendar.MONTH, monthOfYear)
            dateTimeStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }
    private var ts = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateTimeStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateTimeStart.set(Calendar.MINUTE, minute)
        setInitialDateTime()
    }
    private var df = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
        dateTimeFinish.set(Calendar.YEAR, year)
        dateTimeFinish.set(Calendar.MONTH, monthOfYear)
        dateTimeFinish.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        setInitialDateTime()
    }
    private var tf = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateTimeFinish.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateTimeFinish.set(Calendar.MINUTE, minute)
        setInitialDateTime()
    }

    private fun setInitialDateTime(){
        val sdfDate = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        val sdfTime = SimpleDateFormat("HH:mm", Locale("ru"))

        val dateStartString: String = sdfDate.format(dateTimeStart.time)
        val timeStartString: String = sdfTime.format(dateTimeStart.time)
        val dateFinishString: String = sdfDate.format(dateTimeFinish.time)
        val timeFinishString: String = sdfTime.format(dateTimeFinish.time)

        binder.btnStartDate.text = dateStartString
        binder.btnStartTime.text = timeStartString
        binder.btnFinishDate.text = dateFinishString
        binder.btnFinishTime.text = timeFinishString
    }
}