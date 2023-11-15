package com.example.veggieneighbors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class productRecyclerAdapter(val productPostList: List<ProductPostData>): RecyclerView.Adapter<productRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productPostList.size
    }

    override fun onBindViewHolder(holder: productRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = productPostList[position].title
        holder.farm.text = productPostList[position].farm
        holder.price.text = productPostList[position].price
        holder.unit.text = productPostList[position].unit
//        holder.img.text = productPostList[position].img
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.ProductPostTitle)
        val farm: TextView = itemView.findViewById(R.id.ProductPostFarm)
        val unit: TextView = itemView.findViewById(R.id.ProductPostUnit)
        val price: TextView = itemView.findViewById(R.id.ProductPostPrice)
//        val img: TextView = itemView.findViewById(R.id.ProductPostImg)

    }
}