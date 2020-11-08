package com.app.graham.partner.shop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.shop.ShopListActivity
import com.app.graham.partner.shop.pozo.ShoplistPozo
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ShoplistAdapter(val shoplist: ArrayList<ShoplistPozo>?, val shopfragment: ShopListActivity) : RecyclerView.Adapter<ShoplistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.shoplist_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        shoplist?.get(position)?.let { holder.bindItems(it) }
        holder.shoplist_name.text = shoplist!!.get(position).shopname
        holder.shoplist_mobile.text = shoplist!!.get(position).shopmobile
        holder.shoplist_address.text = shoplist!!.get(position).shopaddress
        Glide.with(shopfragment).load(shoplist?.get(position)!!.shopimage).into(holder.shoplist_image);


    }

    override fun getItemCount(): Int {
        return shoplist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoplist_name = itemView.findViewById(R.id.shoplist_name) as TextView
        val shoplist_mobile = itemView.findViewById(R.id.shoplist_mobile) as TextView
        val shoplist_address = itemView.findViewById(R.id.shoplist_address) as TextView
        val shoplist_image =itemView.findViewById<CircleImageView>(R.id.shoplist_image)
        fun bindItems(user: ShoplistPozo) {


        }
    }
}
