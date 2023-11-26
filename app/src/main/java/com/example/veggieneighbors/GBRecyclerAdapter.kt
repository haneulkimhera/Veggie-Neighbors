package com.example.veggieneighbors

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.veggieneighbors.databinding.GbPostBinding
import com.example.veggieneighbors.databinding.ProductPostBinding
import com.google.firebase.storage.FirebaseStorage

class GBRecyclerAdapter(val GBPostList: List<GBPostData>): RecyclerView.Adapter<GBRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GBRecyclerAdapter.ViewHolder {
        val binding = GbPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.gb_post, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return GBPostList.size
    }

    override fun onBindViewHolder(holder: GBRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = GBPostList[position].title
        holder.username.text = GBPostList[position].username
        holder.price.text = GBPostList[position].price
        holder.participate.text = GBPostList[position].participate

        holder.bind(GBPostList[position])

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val pathReference = storageReference.child("Product Posts Res/${GBPostList[position].img}")
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

    inner class ViewHolder(val binding: GbPostBinding): RecyclerView.ViewHolder(binding.root) {
        val title: TextView = itemView.findViewById(R.id.GBPostTitle)
        val username: TextView = itemView.findViewById(R.id.GBPostUsername)
        val price: TextView = itemView.findViewById(R.id.GBPostPrice)
        val participate: TextView = itemView.findViewById(R.id.GBParticipate)
        val image: ImageView = binding.GBPostImage

        private val context = binding.root.context
        fun bind(item: GBPostData) {

            binding.root.setOnClickListener {
                Intent(context, GBDetails::class.java).apply {
                    Log.d("ITM", "A GB post is clicked")

                    putExtra("title", item.title)
                    putExtra("username", item.username)
                    putExtra("unit", item.unit)
                    putExtra("price", item.price)
                    putExtra("productId", item.productId)
                    putExtra("participate", item.participate)
                    putExtra("img", item.img)
                    putExtra("description", item.description)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }.run { context.startActivity(this) }
            }

        }
    }
}