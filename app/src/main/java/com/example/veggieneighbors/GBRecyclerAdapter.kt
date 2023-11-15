package com.example.veggieneighbors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GBRecyclerAdapter(val GBPostList: List<GBPostData>): RecyclerView.Adapter<GBRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GBRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gb_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return GBPostList.size
    }

    override fun onBindViewHolder(holder: GBRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = GBPostList[position].title
        holder.username.text = GBPostList[position].username
        holder.price.text = GBPostList[position].price
        holder.participate.text = GBPostList[position].participate
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.GBPostTitle)
        val username: TextView = itemView.findViewById(R.id.GBPostUsername)
        val price: TextView = itemView.findViewById(R.id.GBPostPrice)
        val participate: TextView = itemView.findViewById(R.id.GBParticipate)
    }
}