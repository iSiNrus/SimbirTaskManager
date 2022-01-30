package ru.barsik.simbirtaskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_table_item.view.*

class TaskAdapter(private val nodes: List<TableNode>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

     inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         val time: TextView = itemView.tv_time
         val taskName: TextView = itemView.tv_task_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_table_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.time.text = String.format("%02d:00\n-\n%02d:00", nodes[position].time, nodes[position].time+1 )
        holder.taskName.text = nodes[position].task?.name?:"Записей нет"
    }

    override fun getItemCount(): Int = nodes.size
}

