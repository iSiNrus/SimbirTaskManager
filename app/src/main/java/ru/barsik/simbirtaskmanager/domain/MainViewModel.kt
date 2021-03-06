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
        repo.getTasksFromRealm(date).subscribe({ tasks ->
            val nodesList = ArrayList<TableNode>()
            (0..23).forEach {
                nodesList.add(TableNode(it))
            }
            nodesListLiveData.postValue(ArrayList())
            for(t in tasks)
                nodesList[t.getCalendarStart().get(Calendar.HOUR_OF_DAY)]
                    .also {
                        it.isBusy = true
                        it.task = t
                    }
            nodesListLiveData.postValue(nodesList)
        }, {})

    }
}