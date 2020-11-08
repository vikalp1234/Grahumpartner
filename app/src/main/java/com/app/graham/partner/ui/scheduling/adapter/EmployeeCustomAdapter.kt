package com.app.graham.partner.ui.scheduling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.graham.partner.R
import com.app.graham.partner.pozo.EmployeelistPozo

class EmployeeCustomAdapter (val context: Context, var categorieslist: List<EmployeelistPozo>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custome_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = categorieslist.get(position).name
        return view
    }

    override fun getItem(position: Int): Any? {
        return categorieslist[position];
    }

    override fun getCount(): Int {
        return categorieslist.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.spinner_itemtxt) as TextView

        }
    }

}