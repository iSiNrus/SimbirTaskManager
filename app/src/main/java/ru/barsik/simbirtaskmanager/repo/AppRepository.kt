package ru.barsik.simbirtaskmanager.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import ru.barsik.simbirtaskmanager.model.Task
import ru.barsik.simbirtaskmanager.model.TaskRealm
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class AppRepository(private val ctx: Context) {

    private val realmVersion = 1L

    private var config: RealmConfiguration? = null

    private fun getConfig(): RealmConfiguration {
        if (config == null) {
            config = RealmConfiguration.Builder()
                .schemaVersion(realmVersion)
                .build()
        }
        return config!!
    }


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
                    it.getCalendarStart()
                        .get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR))
        }

        return tasks
    }

    fun getTasksFromRealm() : Single<List<Task>> {
        val single = Single.create<List<Task>> { emitter ->
            val realm = Realm.getInstance(getConfig())
            val list = mutableListOf<Task>()
            realm.executeTransactionAsync { realmTrans ->
                list.addAll(
                    realmTrans
                        .where(TaskRealm::class.java)
                        .findAll()
                        .map { mapTask(it) }
                )
                emitter.onSuccess(list)
            }
        }
        return single
    }

    fun getTasksFromRealm(date: Calendar) : Single<List<Task>> {
        val single = Single.create<List<Task>> { emitter ->
            val realm = Realm.getInstance(getConfig())
            val list = mutableListOf<Task>()
            realm.executeTransactionAsync { realmTrans ->
                list.addAll(
                    realmTrans
                        .where(TaskRealm::class.java)
                        .findAll()
                        .map { mapTask(it) }
                )
                list.removeIf {
                    !(it.getCalendarStart().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                            it.getCalendarStart()
                                .get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR))
                }
                emitter.onSuccess(list)
            }
        }
        return single
    }

    fun saveTask(task: Task): Single<Int> {

        return Single.create<Int> { emitter->
            val realm = Realm.getInstance(getConfig())
            val taskR = TaskRealm(
                id = task.id,
                name = task.name,
                date_start = task.date_start,
                date_finish = task.date_finish,
                description = task.description
            )
            realm.executeTransactionAsync({ realmTransaction ->
                realmTransaction.insertOrUpdate(taskR)
            },{ emitter.onSuccess(1) },{emitter.onError(it)})
        }.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())

    }

    private fun mapTask(taskR: TaskRealm): Task {
        return Task(
            name = taskR.name,
            date_start = taskR.date_start,
            date_finish = taskR.date_finish,
            description = taskR.description
        )
    }

}

