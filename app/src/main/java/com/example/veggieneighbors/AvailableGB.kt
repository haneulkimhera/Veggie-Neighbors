package com.example.veggieneighbors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.veggieneighbors.databinding.ActivityAvailableGbBinding
import com.example.veggieneighbors.databinding.ActivityGbdetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AvailableGB : AppCompatActivity() {

    lateinit var binding: ActivityAvailableGbBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAvailableGbBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.productID.text = intent.getStringExtra("productId")

        showGBPostRecycler()
    }

    fun showGBPostRecycler() {
        val db = FirebaseFirestore.getInstance()
        val GBPostList = mutableListOf<GBPostData>()
        val adapter = AvailableGBRecyclerAdapter(GBPostList)
        val productId = intent.getStringExtra("productId")

        Log.d("ITM", "productId from intent : $productId")

        binding.availableGBRecycler.adapter = adapter
        binding.availableGBRecycler.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = db.collection("GB Posts").get().await()

                GBPostList.clear()
                for (document in result) {
                    val item = GBPostData(
                        document.getString("title") ?: "",
                        document.getString("username") ?: "",
                        document.getString("price") ?: "",
                        document.getString("unit") ?: "",
                        document.getString("participate") ?: "",
                        document.getString("region") ?: "",
                        document.getString("productId") ?: "",
                        document.getString("img") ?:"",
                        document.getString("description") ?:""
                    )

                    if(item.productId == productId){
                        GBPostList.add(item)
                        Log.d("ITM","available item added, $item")
                    }
                }
                Log.d("ITM","available items : $GBPostList")

                adapter.notifyDataSetChanged()

            } catch (exception: Exception) {
                Log.d("ITM", "Error getting documents: $exception")
            }
        }

    }
}