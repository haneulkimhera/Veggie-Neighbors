package com.example.veggieneighbors

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.veggieneighbors.databinding.ProductCategoryBinding
import com.example.veggieneighbors.databinding.ProductPostBinding
import com.google.firebase.storage.FirebaseStorage

class productRecyclerAdapter(val productPostList: List<ProductPostData>): RecyclerView.Adapter<productRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productRecyclerAdapter.ViewHolder {
        val binding = ProductPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_post, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productPostList.size
    }

    override fun onBindViewHolder(holder: productRecyclerAdapter.ViewHolder, position: Int) {

        // set values of the view items
        holder.title.text = productPostList[position].title
        holder.farm.text = productPostList[position].farm
        holder.price.text = productPostList[position].price
        holder.unit.text = productPostList[position].unit

        holder.bind(productPostList[position])

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val pathReference = storageReference.child("Product Posts Res/${productPostList[position].img}")
        Log.d("ITM", "Image path: $pathReference")

        pathReference.downloadUrl.addOnCompleteListener { uri ->
            Log.d("ITM", "Image URI: $uri")

            Glide.with(holder.itemView.context)
                .load(uri.result)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .signature(ObjectKey(System.currentTimeMillis()))
                .into(holder.image)
        }.addOnFailureListener { exception ->
            Log.d("ITM", "Error getting download URL", exception)
        }
        Log.d("ITM","title: ${holder.title.text}, context: ${holder.image.context}")
    }

    inner class ViewHolder(val binding: ProductPostBinding): RecyclerView.ViewHolder(binding.root) {
        val title: TextView = itemView.findViewById(R.id.ProductPostTitle)
        val farm: TextView = itemView.findViewById(R.id.ProductPostFarm)
        val unit: TextView = itemView.findViewById(R.id.ProductPostUnit)
        val price: TextView = itemView.findViewById(R.id.ProductPostPrice)
        val image: ImageView = itemView.findViewById(R.id.ProductPostImg)

        private val context = binding.root.context

        fun bind(item: ProductPostData) {

            binding.root.setOnClickListener {
                Intent(context, ProductDetails::class.java).apply {
                    Log.d("ITM", "item is clicked")

                    putExtra("title", item.title)
                    putExtra("farm", item.farm)
                    putExtra("unit", item.unit)
                    putExtra("price", item.price)
                    putExtra("img", item.img)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }.run { context.startActivity(this) }
            }

        }

    }

}