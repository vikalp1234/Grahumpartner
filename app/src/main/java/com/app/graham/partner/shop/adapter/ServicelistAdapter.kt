package com.app.graham.partner.shop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.shop.ShopListActivity
import com.app.graham.partner.shop.pozo.ShoplistPozo
import com.app.graham.partner.ui.setting.ServiceLayout
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ServicelistAdapter (val servicelist: ArrayList<ServicelistPozo>?, val shopfragment: ServiceLayout) : RecyclerView.Adapter<ServicelistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.servicelist_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.shoplist_name.text = servicelist!!.get(position).title
    }

    override fun getItemCount(): Int {
        return servicelist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoplist_name = itemView.findViewById(R.id.servicelist_name) as TextView

    }
}