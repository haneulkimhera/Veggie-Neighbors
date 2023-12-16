package com.example.veggieneighbors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
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
        cancelBtnClickListener()

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
            Log.d("ITM","gatherBtn is clicked")

            val title = binding.editTextTitle
            val participate = binding.editTextNumber
            val unit = binding.editTextUnit
            val price = binding.editTextPrice
            val region = binding.editTextRegion

            var detailsList = mutableMapOf(Pair(title,"Title"),Pair(participate,"Number of Participants"),Pair(unit,"Unit"),Pair(price,"Price"),Pair(region,"Location"))

            if(isAnyNull(detailsList)==true){
                Log.d("ITM","detail is missed")
                detailsCheck(detailsList)
            }
            else{
                val db = FirebaseFirestore.getInstance()
                val newGB = createNewGB()
                val newDoc = db.collection("GB Posts").document()
                newDoc.set(newGB)

                finish()
            }
        }
    }

    fun cancelBtnClickListener(){
        binding.cancelBtn.setOnClickListener {
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

    fun detailsCheck(detailsList:MutableMap<EditText,String>){
        Log.d("ITM","detailsCheck() is called")
        loop@ for (detail in detailsList){
            val editText = detail.key
            val name = detail.value
            if (editText.text.toString().equals("") || editText == null){
                Log.d("ITM","detailsCheck() is called - $name is missed")
                editText.hint = "* Please fill in the blank"
                Toast.makeText(this, "$name is missed. Please Check again", Toast.LENGTH_SHORT).show()
                break@loop
            }
        }
    }

    fun isAnyNull(detailsList:MutableMap<EditText,String>):Boolean{
        Log.d("ITM","isAnyNull() is called")
        var isNull = false
        for (detail in detailsList){
            val editText = detail.key
            if(editText.text.toString().equals("") || editText == null){
                isNull = true
            }
        }
        return isNull
    }
}