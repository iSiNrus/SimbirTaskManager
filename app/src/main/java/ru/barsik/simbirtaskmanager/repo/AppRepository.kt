package ru.barsik.simbirtaskmanager.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.barsik.simbirtaskmanager.model.Task
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class AppRepository(private val ctx: Context) {

    private val realmVersion = 1L

    private var config: RealmConfiguration? = null

    private fun getConfig(): RealmConfiguration {
        if(config == null) {
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
                    it.getCalendarStart().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR))
        }

        return tasks
    }

    fun getTasksFromRealm() : Single<List<Task>> {
        val realm = Realm.getInstance(getConfig())
        val single : Single<List<Task>> = Single.create{ emitter ->
            val results = realm.where(Task::class.java).findAll()
            emitter.onSuccess(realm.copyFromRealm(results))
        }
        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        return single
    }

}

