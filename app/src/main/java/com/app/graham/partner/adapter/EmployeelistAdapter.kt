package com.app.graham.partner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.pozo.EmployeelistPozo
import com.app.graham.partner.ui.employee.EmployeeFragment
import com.bumptech.glide.Glide

class EmployeelistAdapter(val employeelist: ArrayList<EmployeelistPozo>?, val employeeFragment: EmployeeFragment) : RecyclerView.Adapter<EmployeelistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.employee_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        employeelist?.get(position)?.let { holder.bindItems(it) }
        holder.name.text = employeelist!!.get(position).name
        holder.empspeciality.text =employeelist!!.get(position).specialist
        Glide.with(employeeFragment).load(employeelist?.get(position)!!.image).into(holder.empprofile_image);
    }

    override fun getItemCount(): Int {
        return employeelist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name  = itemView.findViewById(R.id.emp_name) as TextView
        val empspeciality = itemView.findViewById(R.id.empspeciality) as TextView
        val empprofile_image =itemView.findViewById<ImageView>(R.id.empprofile_image)
        fun bindItems(user: EmployeelistPozo) {


        }
    }
}