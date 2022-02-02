package ru.barsik.simbirtaskmanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class TaskRealm(

    @PrimaryKey
    var id: String = ObjectId.get().toHexString(),

    var date_finish: String = "Unknown",
    var date_start: String = "Unknown",
    var description: String = "Unknown",
    var name: String = "Unknown"

) : RealmObject()