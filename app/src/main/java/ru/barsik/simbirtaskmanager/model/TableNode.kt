package ru.barsik.simbirtaskmanager.model

import ru.barsik.simbirtaskmanager.model.Task

data class TableNode(
    /*от 0 до 23*/
    var time: Int = 0,
    var task: Task? = null,
    var isBusy : Boolean = false
) {
}