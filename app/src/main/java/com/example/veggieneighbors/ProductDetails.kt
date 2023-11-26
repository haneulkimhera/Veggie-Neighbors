package com.example.veggieneighbors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.veggieneighbors.databinding.ActivityProductDetailsBinding
import com.google.firebase.storage.FirebaseStorage

class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewContents(binding)

    }

    fun setViewContents(binding:ActivityProductDetailsBinding){
        val title = intent.getStringExtra("title")
        val farm = intent.getStringExtra("farm")
        val unit = intent.getStringExtra("unit")
        val price = intent.getStringExtra("price")
        val img = intent.getStringExtra("img")

        binding.productTitleTxt.text = title
        binding.productFarmTxt.text = farm
        binding.productUnitTxt.text = unit
        binding.productPriceTxt.text = price

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val pathReference = storageReference.child("Product Posts Res/${img}")
        Log.d("ITM", "Image path: $pathReference")

        pathReference.downloadUrl.addOnCompleteListener { uri ->
            Log.d("ITM", "Image URI: $uri")

            Glide.with(this)
                .load(uri.result)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .signature(ObjectKey(System.currentTimeMillis()))
                .into(binding.productImage)
        }.addOnFailureListener { exception ->
            Log.d("ITM", "Error getting download URL", exception)
        }
    }

    fun availableBtnClickListener(){

    }

    fun createBtnClickListener(){

    }


}