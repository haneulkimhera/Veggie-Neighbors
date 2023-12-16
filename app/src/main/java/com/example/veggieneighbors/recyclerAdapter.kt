package com.example.veggieneighbors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.veggieneighbors.databinding.GbPostBinding

class recyclerAdapter(val GBPosts:MutableList<GBPostData>): RecyclerView.Adapter<recyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GbPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val GBPost = GBPosts.get(position)
        holder.bind(GBPost)
    }

    override fun getItemCount(): Int {
        return GBPosts.size
    }

    class ViewHolder(val binding: GbPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(GBPost: GBPostData) {
            binding.GBPostTitle.text = GBPost.title
            binding.GBPostUsername.text = GBPost.username
            binding.GBPostPrice.text = GBPost.price
        }
    }
}