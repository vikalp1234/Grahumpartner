package com.app.graham.partner.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.graham.partner.R
import com.app.graham.partner.ui.gallery.GalleryFragment
import com.app.graham.partner.ui.gallery.pozo.GallerylistPozo
import com.bumptech.glide.Glide

class GallerylistAdapter(val gallerylist: ArrayList<GallerylistPozo>?,val galleryFragment: GalleryFragment) : RecyclerView.Adapter<GallerylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        gallerylist?.get(position)?.let { holder.bindItems(it) }
        Glide.with(galleryFragment).load(gallerylist?.get(position)!!.imageName).into(holder.gallery_img)


    }

    override fun getItemCount(): Int {
        return gallerylist!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gallery_img  = itemView.findViewById(R.id.gallery_img) as ImageView
        fun bindItems(user: GallerylistPozo) {


        }
    }
}