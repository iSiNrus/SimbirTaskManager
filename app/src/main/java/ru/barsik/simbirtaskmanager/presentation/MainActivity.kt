package ru.barsik.simbirtaskmanager.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.barsik.simbirtaskmanager.model.TableNode
import ru.barsik.simbirtaskmanager.databinding.ActivityMainBinding
import ru.barsik.simbirtaskmanager.domain.MainViewModel
import ru.barsik.simbirtaskmanager.model.Task
import ru.barsik.simbirtaskmanager.repo.AppRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        public const val EXTRA_TASK = "EXTRA_TASK"
    }

    private val TAG = "MainActivity"
    private lateinit var binder: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var nodes : MutableLiveData<ArrayList<TableNode>>

    private var repo: AppRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        viewModel = MainViewModel(applicationContext)
        nodes = viewModel.getNodesListLiveData()
        viewModel.getNodesList(Calendar.getInstance())
        binder.recyclerview.layoutManager = LinearLayoutManager(this)
        binder.recyclerview.adapter = TaskAdapter(nodes.value!!, applicationContext)
        binder.recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binder.calendarView.setOnDayClickListener {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            val date: String = sdf.format(it.calendar.time)
            binder.tvChoosenDate.text = date
            viewModel.getNodesList(it.calendar)
        }


        viewModel.getNodesListLiveData().observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "onCreate: Наблюдаю изменения...")
            (binder.recyclerview.adapter as TaskAdapter).updateNodes(nodes.value!!)
        })

        repo = AppRepository(applicationContext)
        logTasksFromDB()
        binder.fab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getNodesList(Calendar.getInstance())
    }
    private fun logTasksFromDB(){
        repo!!.getTasksFromRealm().subscribe({
            Log.d(TAG, "subscribe: получил данные")
            for(t in it) Log.d(TAG, "subscribe: ${t.name} ")
        },{
            throw it
        })
    }
}