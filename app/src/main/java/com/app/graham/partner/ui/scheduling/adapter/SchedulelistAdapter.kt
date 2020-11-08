package com.app.graham.partner.ui.scheduling.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.ui.scheduling.pozo.SchedulelistPozo

class SchedulelistAdapter(val employeelist: ArrayList<SchedulelistPozo>?) : RecyclerView.Adapter<SchedulelistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedulelist_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        employeelist?.get(position)?.let { holder.bindItems(it) }
        holder.starttime_tv.text = "Start Time : " + employeelist!!.get(position).start_time
        holder.endtime_tv.text = "End Time : " + employeelist!!.get(position).end_time
        holder.timeslot_tv.text = "Time Slot : " + employeelist!!.get(position).time_slot
        holder.weekoff_tv.text = "Weekly off : " + employeelist!!.get(position).off_day

        //  Glide.with(this).load(employeelist!!.get(position).).into(holder.image);

    }

    override fun getItemCount(): Int {
        return employeelist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val starttime_tv = itemView.findViewById(R.id.starttime_tv) as TextView
        val endtime_tv = itemView.findViewById(R.id.endtime_tv) as TextView
        val timeslot_tv = itemView.findViewById(R.id.timeslot_tv) as TextView
        val weekoff_tv = itemView.findViewById(R.id.weekoff_tv) as TextView

        //   val empspeciality = itemView.findViewById(R.id.empspeciality) as TextView
        fun bindItems(user: SchedulelistPozo) {


        }
    }
}