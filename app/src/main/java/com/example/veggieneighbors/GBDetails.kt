package com.example.veggieneighbors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.veggieneighbors.databinding.ActivityGbdetailsBinding
import com.example.veggieneighbors.databinding.ActivityProductDetailsBinding
import com.google.firebase.storage.FirebaseStorage

class GBDetails : AppCompatActivity() {

    private lateinit var binding: ActivityGbdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGbdetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewContents(binding)
    }

    fun setViewContents(binding:ActivityGbdetailsBinding){
        val title = intent.getStringExtra("title")
        val username = intent.getStringExtra("username")
        val unit = intent.getStringExtra("unit")
        val price = intent.getStringExtra("price")
        val participate = intent.getStringExtra("participate")
        val img = intent.getStringExtra("img")
        val description = intent.getStringExtra("description")

        binding.GBTitleTxt.text = title
        binding.GBUsernameTxt.text = username
        binding.GBUnitTxt.text = unit
        binding.GBPriceTxt.text = price
        binding.GBParticipateTxt.text = participate
        binding.GBDescTxt.text = description

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
}