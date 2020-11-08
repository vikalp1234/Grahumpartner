package com.app.graham.partner.ui.packagesdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.pozo.EmployeelistPozo

class PackagelistAdapter (val packagelist: ArrayList<PackagelistPozo>?) : RecyclerView.Adapter<PackagelistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.package_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        packagelist?.get(position)
        holder.package_name_tv.text = packagelist!!.get(position).package_name
        holder.package_desc_tv.text = packagelist.get(position).description
        holder.package_discountamount_tv.text = packagelist.get(position).amount
        holder.package_amount_tv.text = packagelist.get(position).mrp

    }

    override fun getItemCount(): Int {
        return packagelist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val package_name_tv  = itemView.findViewById(R.id.package_name_tv) as TextView
        val package_desc_tv = itemView.findViewById(R.id.package_desc_tv) as TextView
        val package_discountamount_tv = itemView.findViewById(R.id.package_discountamount_tv) as TextView
        val package_amount_tv = itemView.findViewById(R.id.package_amount_tv) as TextView

        fun bindItems(user: EmployeelistPozo) {


        }
    }
}