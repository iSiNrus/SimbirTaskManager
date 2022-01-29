package ru.barsik.simbirtaskmanager

data class Task(
    val date_finish: String,
    val date_start: String,
    val description: String,
    val id: Int,
    val name: String
)