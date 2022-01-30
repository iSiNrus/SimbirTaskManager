package ru.barsik.simbirtaskmanager

data class TableNode(
    /*от 0 до 23*/
    var time: Int = 0,
    val task: Task? = null,
    var isBusy : Boolean = false
) {
}