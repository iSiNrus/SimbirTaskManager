package ru.barsik.simbirtaskmanager.presentation

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_table_item.view.*
import ru.barsik.simbirtaskmanager.R
import ru.barsik.simbirtaskmanager.model.TableNode

class TaskAdapter(private var nodes: ArrayList<TableNode>, private var ctx: Context): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val TAG = "TaskAdapter"

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
        if(nodes[position].isBusy) holder.taskName.setBackgroundColor(ctx.getColor(R.color.item_color))
    }

    override fun getItemCount(): Int = nodes.size

    fun updateNodes(nNodes : ArrayList<TableNode>){
        Log.d(TAG, "updateNodes: ${nodes.size}")
        this.nodes = nNodes
        this.notifyDataSetChanged()
    }
}

