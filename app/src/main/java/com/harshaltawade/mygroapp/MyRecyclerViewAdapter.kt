package com.harshaltawade.mygroapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(val glist : MutableList<Info>):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_items,parent,false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val glist_item = glist[position]
        holder.gname.text = glist_item.gname
        holder.gqty.text = glist_item.gqty
    }

    override fun getItemCount(): Int {
        return glist.size
    }

}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val gname = view.findViewById<TextView>(R.id.tvname)
        val gqty = view.findViewById<TextView>(R.id.tvqty)
}