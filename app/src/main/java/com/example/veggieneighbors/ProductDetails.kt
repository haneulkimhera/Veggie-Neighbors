package com.example.veggieneighbors

import android.content.Intent
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
        availableBtnClickListener()
        createBtnClickListener()
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
        binding.availableBtn.setOnClickListener {
            Log.d("ITM", "availableBtnClicked")
            startActivity(Intent(this,AvailableGB::class.java).putExtra("productId",binding.productTitleTxt.text))
        }
    }

    fun createBtnClickListener(){
        binding.createBtn.setOnClickListener {
            Log.d("ITM","createBtn is clicked")
            startActivity(Intent(this, CreatePost::class.java).apply {
                putExtra("productId",binding.productTitleTxt.text)
                putExtra( "productPrice",binding.productPriceTxt.text)
                putExtra("productImg",intent.getStringExtra("img"))
            })

//            val intent = Intent(this, CreatePost::class.java).apply {
//                putExtra("productId", binding.productTitleTxt.text.toString())
//                putExtra("productPrice", binding.productPriceTxt.text.toString())
////                putExtra("productImg", intent.getStringExtra("img"))
//            }

//            startActivity(intent)

//            startActivity(Intent(this,CreatePost::class.java).putExtra("productId",binding.productTitleTxt.text))
        }
    }
}