package com.example.veggieneighbors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FridgeAdapter(private val productList: List<FridgeItem>) : RecyclerView.Adapter<FridgeAdapter.ProductViewHolder>() {

    class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // Bind views here
        val imageView: ImageView = view.findViewById(R.id.product_image)
        val nameTextView: TextView = view.findViewById(R.id.product_name)
        val bestbeforTextView: TextView = view.findViewById(R.id.best_before_date)
        val purchaseddateTextView: TextView = view.findViewById(R.id.purchased_date)
    // Initialize other views if necessary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fridge, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = productList[position]
        holder.nameTextView.text = productItem.name
        holder.purchaseddateTextView.text = "Purchased on: ${productItem.purchasedDate}"
        holder.bestbeforTextView.text = "Best Before: ${productItem.bestBefore}"

        // Glide로 다운로드 URL을 사용하여 이미지를 로드합니다.
        Glide.with(holder.imageView.context)
            .load(productItem.imgUrl) // 이제 여기에 다운로드 URL을 사용합니다.
            .into(holder.imageView)
    }


    override fun getItemCount() = productList.size
}
