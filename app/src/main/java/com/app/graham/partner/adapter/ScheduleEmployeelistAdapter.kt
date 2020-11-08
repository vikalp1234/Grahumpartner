package com.app.graham.partner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.pozo.EmployeelistPozo

class ScheduleEmployeelistAdapter (val employeelist: ArrayList<EmployeelistPozo>?) : RecyclerView.Adapter<ScheduleEmployeelistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scheduleemployee_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        employeelist?.get(position)?.let { holder.bindItems(it) }
        holder.name.text = employeelist!!.get(position).name
        //  Glide.with(this).load(employeelist!!.get(position).).into(holder.image);

    }

    override fun getItemCount(): Int {
        return employeelist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name  = itemView.findViewById(R.id.schedule_empname) as TextView
        val image  = itemView.findViewById(R.id.scheduleemp_image) as ImageView

     //   val empspeciality = itemView.findViewById(R.id.empspeciality) as TextView
        fun bindItems(user: EmployeelistPozo) {


        }
    }
}