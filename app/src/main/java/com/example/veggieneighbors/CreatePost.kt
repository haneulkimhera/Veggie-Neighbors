package com.example.veggieneighbors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.veggieneighbors.databinding.ActivityAvailableGbBinding
import com.example.veggieneighbors.databinding.ActivityCreatePostBinding
import com.google.firebase.firestore.FirebaseFirestore

class CreatePost : AppCompatActivity() {

    lateinit var binding: ActivityCreatePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        calculatePrice()
        gatherBtnClickListener()

    }

    fun calculatePrice(){
        binding.calculate.setOnClickListener {
            val productTitle = intent.getStringExtra("productId")
            val productPrice = intent.getStringExtra("productPrice")?.toFloat()
            val participate = binding.editTextNumber.text.toString().toInt()
            val price = productPrice?.div(participate)
            binding.editTextPrice.setText(price.toString())
        }
    }
    fun gatherBtnClickListener(){
        binding.gatherBtn.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val newGB = createNewGB()
            val newDoc = db.collection("GB Posts").document()
            newDoc.set(newGB)

            finish()
        }
    }

    fun createNewGB():GBPostData{

        var productImg = intent.getStringExtra("productImg")
        val productTitle = intent.getStringExtra("productId")
        var productPrice = intent.getStringExtra("productPrice")?.toFloat()

        val username = "hera"
        val title = binding.editTextTitle.text.toString()
        val participate = binding.editTextNumber.text.toString()
        val price = binding.editTextPrice.text.toString()
        val unit = binding.editTextUnit.text.toString()
        val description = binding.editTextTextMultiLine.text.toString()
        val region = binding.editTextRegion.text.toString()
        val productId = productTitle.toString()
        val img = productImg.toString()

        val newGB = GBPostData(title,username,price,unit,participate,region, productId, img, description)

        return newGB
    }


}