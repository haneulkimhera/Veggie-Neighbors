package com.example.veggieneighbors

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.veggieneighbors.databinding.ProductCategoryBinding
import kotlinx.coroutines.selects.select

interface CategorySelectionListener {
    fun onCategorySelected(category: String)
}

class categoryRecyclerAdapter (val categoryList:ArrayList<String>, private val categorySelectionListener: CategorySelectionListener): RecyclerView.Adapter<categoryRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryRecyclerAdapter.ViewHolder {
        val binding = ProductCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList.get(position)
        holder.bind(category)
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
    inner class ViewHolder(private val binding:ProductCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category:String){
            binding.categoryTxt.text=category
            binding.root.setOnClickListener {
                val selectedCategory = binding.categoryTxt.text as String
                Log.d("ITM","selectedCategory is set : ${selectedCategory}")
                categorySelectionListener.onCategorySelected(selectedCategory)
            }
        }
    }
}
