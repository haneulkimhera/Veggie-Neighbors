package com.example.veggieneighbors


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.veggieneighbors.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showGBPostRecycler()
        categoryBtnClickListener()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun showGBPostRecycler() {
        val db = FirebaseFirestore.getInstance()
        val GBPostList = mutableListOf<GBPostData>()
        val productPostList = mutableListOf<ProductPostData>()
        val adapter = GBRecyclerAdapter(GBPostList)

        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)

        Log.d("ITM", "Button Clicked.")
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val products = db.collection("Product Posts").get().await()
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
                    GBPostList.add(item)
                }

                productPostList.clear()
                for (document in products) {
                    val item = ProductPostData(
                        document.getString("title") ?: "",
                        document.getString("farm") ?: "",
                        document.getString("category") ?: "",
                        document.getString("price") ?: "",
                        document.getString("unit") ?: "",
                        document.getString("img") ?: ""
                    )
                    productPostList.add(item)
                }

                adapter.notifyDataSetChanged()
            } catch (exception: Exception) {
                Log.d("ITM", "Error getting documents: $exception")
            }
        }

    }

    fun categoryBtnClickListener() {
        Log.d("ITM", "buttonClickListner Called!")
        binding.categoryBtn.setOnClickListener {
            val productsFragment = ProductsFragment()  // ProductsFragment의 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fl_container, productsFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}