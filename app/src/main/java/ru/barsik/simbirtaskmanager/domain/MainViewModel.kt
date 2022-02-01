package ru.barsik.simbirtaskmanager.domain

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.barsik.simbirtaskmanager.model.TableNode
import ru.barsik.simbirtaskmanager.repo.AppRepository
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(ctx: Context) : ViewModel() {

    private val TAG = "MainViewModel"
    private val nodesListLiveData = MutableLiveData(ArrayList<TableNode>())
    private val repo = AppRepository(ctx)

    fun getNodesListLiveData() = nodesListLiveData

    fun getNodesList(date: Calendar){
        val tasks = repo.getTasks(date)
        nodesListLiveData.value = ArrayList()
        (0..23).forEach {
            nodesListLiveData.value?.add(TableNode(it))
        }
        for(t in tasks)
            (nodesListLiveData.value?.get(t.getCalendarStart().get(Calendar.HOUR_OF_DAY)) as TableNode)
                .also {
                it.isBusy = true
                it.task = t
            }
    }
}