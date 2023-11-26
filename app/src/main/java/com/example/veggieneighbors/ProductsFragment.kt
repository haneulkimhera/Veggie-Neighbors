package com.example.veggieneighbors

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.veggieneighbors.databinding.FragmentHomeBinding
import com.example.veggieneighbors.databinding.FragmentProductsBinding
import com.example.veggieneighbors.databinding.ProductPostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment : Fragment(), CategorySelectionListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProductsBinding
    private var selectedCategory: String = "Herbs"

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

        binding=FragmentProductsBinding.inflate(inflater)
        showCategoryRecycler()
        showProductRecycler()

        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun showCategoryRecycler(){
        val categoryList = arrayListOf<String>("Organic","Herbs","Fruits","Vegetables","Ugly")

        val categoryRecyclerAdapter = categoryRecyclerAdapter (categoryList,this)
        binding.categoryRecyclerView.adapter = categoryRecyclerAdapter
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onCategorySelected(category: String) {
        selectedCategory = category
        binding.selectedCategoryTxt.text = selectedCategory
        showProductRecycler()
        Log.d("ITM", "Selected Category in onCategorySelected(): $selectedCategory")
    }


    fun showProductRecycler() {
        val db = FirebaseFirestore.getInstance()
        val productPostList = mutableListOf<ProductPostData>()
        val adapter = productRecyclerAdapter(productPostList)

        binding.productRecyclerView.adapter = adapter
        binding.productRecyclerView.layoutManager = GridLayoutManager(context, 2)

        Log.d("ITM", "showProductRecycler() executed")
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = db.collection("Product Posts").get().await()

                productPostList.clear()
                for (document in result) {
                    val item = ProductPostData(
                        document.getString("title") ?: "",
                        document.getString("farm") ?: "",
                        document.getString("category") ?: "",
                        document.getString("price") ?: "",
                        document.getString("unit") ?: "",
                        document.getString("img") ?: ""
                    )
                    Log.d("ITM", "item imported, title:${item.title}, category:${item.category}")

                    if (item.category == selectedCategory) {
                        productPostList.add(item)
                        Log.d("ITM", "item added")
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d("ITM", "adapter notified")

            } catch (exception: Exception) {
                Log.d("ITM", "Error getting documents: $exception")
            }
        }

    }
}


//package com.example.veggieneighbors
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.veggieneighbors.databinding.FragmentHomeBinding
//import com.example.veggieneighbors.databinding.FragmentProductsBinding
//import com.example.veggieneighbors.databinding.ProductPostBinding
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [ProductsFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ProductsFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    lateinit var binding: FragmentProductsBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        binding=FragmentProductsBinding.inflate(inflater)
//        showCategoryRecycler()
//        showProductRecycler()
//
//        return binding.root
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ProductsFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ProductsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//
//    fun showCategoryRecycler(){
//        val categoryList = arrayListOf<String>("Organic","Herbs","Fruits","Vegetables","Ugly")
//
//        val categoryRecyclerAdapter = categoryRecyclerAdapter (categoryList)
//        binding.categoryRecyclerView.adapter = categoryRecyclerAdapter
//        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context,
//            LinearLayoutManager.HORIZONTAL, false)
//
//    }
//
//    fun showProductRecycler(){
//        val db = FirebaseFirestore.getInstance()
//        val productPostList = mutableListOf<ProductPostData>()
//        val adapter = productRecyclerAdapter(productPostList)
//
//        binding.productRecyclerView.adapter = adapter
//        binding.productRecyclerView.layoutManager = GridLayoutManager(context, 2)
//
//        Log.d("ITM", "showProductRecycler() executed")
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val result = db.collection("Product Posts").get().await()
//
//                productPostList.clear()
//                for (document in result) {
//                    val item = ProductPostData(
//                        document.getString("title") ?: "",
//                        document.getString("farm") ?: "",
//                        document.getString("category") ?: "",
//                        document.getString("price") ?: "",
//                        document.getString("unit") ?: "",
//                        document.getString("img") ?: ""
//                    )
//
//                    productPostList.add(item)
//                    Log.d("ITM", "item added")
//                }
//                adapter.notifyDataSetChanged()
//                Log.d("ITM", "adapter notified")
//
//            } catch (exception: Exception) {
//                Log.d("ITM", "Error getting documents: $exception")
//            }
//        }
//
//    }
//}
//
//


//package com.example.veggieneighbors
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.signature.ObjectKey
//import com.example.veggieneighbors.databinding.FragmentHomeBinding
//import com.example.veggieneighbors.databinding.FragmentProductsBinding
//import com.example.veggieneighbors.databinding.ProductPostBinding
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.ktx.storage
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [ProductsFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ProductsFragment : Fragment(), CategorySelectionListener {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    lateinit var binding: FragmentProductsBinding
//    private var selectedCategory: String = "Herbs"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        binding=FragmentProductsBinding.inflate(inflater)
//        showCategoryRecycler()
//        showProductRecycler()
//
//        return binding.root
//    }
//
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ProductsFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ProductsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//
//    fun showCategoryRecycler(){
//        val categoryList = arrayListOf<String>("Organic","Herbs","Fruits","Vegetables","Ugly")
//
//        val categoryRecyclerAdapter = categoryRecyclerAdapter (categoryList,this)
//        binding.categoryRecyclerView.adapter = categoryRecyclerAdapter
//        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context,
//            LinearLayoutManager.HORIZONTAL, false)
//
//    }
//
//    override fun onCategorySelected(category: String) {
//        selectedCategory = category
//        binding.selectedCategoryTxt.text = selectedCategory
//        showProductRecycler()
//        Log.d("ITM", "Selected Category in onCategorySelected(): $selectedCategory")
//    }
//
//    fun showProductRecycler() {
//        val db = FirebaseFirestore.getInstance()
//        val productPostList = mutableListOf<ProductPostData>()
//        val adapter = productRecyclerAdapter(productPostList)
//
//        binding.productRecyclerView.adapter = adapter
//        binding.productRecyclerView.layoutManager = GridLayoutManager(context, 2)
//
//        Log.d("ITM", "showProductRecycler() executed")
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val result = db.collection("Product Posts").get().await()
//
//                productPostList.clear()
//                for (document in result) {
//                    val item = ProductPostData(
//                        document.getString("title") ?: "",
//                        document.getString("farm") ?: "",
//                        document.getString("category") ?: "",
//                        document.getString("price") ?: "",
//                        document.getString("unit") ?: "",
//                        document.getString("img") ?: ""
//                    )
//                    Log.d("ITM", "item imported, title:${item.title}, category:${item.category}")
//
//                    if (item.category == selectedCategory) {
//                        productPostList.add(item)
//                        Log.d("ITM", "item added")
//                    }
//                }
//                adapter.notifyDataSetChanged()
//                Log.d("ITM", "adapter notified")
//
//            } catch (exception: Exception) {
//                Log.d("ITM", "Error getting documents: $exception")
//            }
//        }
//
//    }
//}
//
//
////package com.example.veggieneighbors
////
////import android.os.Bundle
////import android.util.Log
////import androidx.fragment.app.Fragment
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import androidx.lifecycle.lifecycleScope
////import androidx.recyclerview.widget.GridLayoutManager
////import androidx.recyclerview.widget.LinearLayoutManager
////import com.example.veggieneighbors.databinding.FragmentHomeBinding
////import com.example.veggieneighbors.databinding.FragmentProductsBinding
////import com.example.veggieneighbors.databinding.ProductPostBinding
////import com.google.firebase.firestore.FirebaseFirestore
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.GlobalScope
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.tasks.await
////import kotlinx.coroutines.withContext
////
////// TODO: Rename parameter arguments, choose names that match
////// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////private const val ARG_PARAM1 = "param1"
////private const val ARG_PARAM2 = "param2"
////
/////**
//// * A simple [Fragment] subclass.
//// * Use the [ProductsFragment.newInstance] factory method to
//// * create an instance of this fragment.
//// */
////class ProductsFragment : Fragment() {
////    // TODO: Rename and change types of parameters
////    private var param1: String? = null
////    private var param2: String? = null
////
////    lateinit var binding: FragmentProductsBinding
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
////    }
////
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////
////        binding=FragmentProductsBinding.inflate(inflater)
////        showCategoryRecycler()
////        showProductRecycler()
////
////        return binding.root
////    }
////
////    companion object {
////        /**
////         * Use this factory method to create a new instance of
////         * this fragment using the provided parameters.
////         *
////         * @param param1 Parameter 1.
////         * @param param2 Parameter 2.
////         * @return A new instance of fragment ProductsFragment.
////         */
////        // TODO: Rename and change types and number of parameters
////        @JvmStatic
////        fun newInstance(param1: String, param2: String) =
////            ProductsFragment().apply {
////                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
////                }
////            }
////    }
////
////    fun showCategoryRecycler(){
////        val categoryList = arrayListOf<String>("Organic","Herbs","Fruits","Vegetables","Ugly")
////
////        val categoryRecyclerAdapter = categoryRecyclerAdapter (categoryList)
////        binding.categoryRecyclerView.adapter = categoryRecyclerAdapter
////        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context,
////            LinearLayoutManager.HORIZONTAL, false)
////
////    }
////
////    fun showProductRecycler(){
////        val db = FirebaseFirestore.getInstance()
////        val productPostList = mutableListOf<ProductPostData>()
////        val adapter = productRecyclerAdapter(productPostList)
////
////        binding.productRecyclerView.adapter = adapter
////        binding.productRecyclerView.layoutManager = GridLayoutManager(context, 2)
////
////        Log.d("ITM", "showProductRecycler() executed")
////        GlobalScope.launch(Dispatchers.Main) {
////            try {
////                val result = db.collection("Product Posts").get().await()
////
////                productPostList.clear()
////                for (document in result) {
////                    val item = ProductPostData(
////                        document.getString("title") ?: "",
////                        document.getString("farm") ?: "",
////                        document.getString("category") ?: "",
////                        document.getString("price") ?: "",
////                        document.getString("unit") ?: "",
////                        document.getString("img") ?: ""
////                    )
////
////                    productPostList.add(item)
////                    Log.d("ITM", "item added")
////                }
////                adapter.notifyDataSetChanged()
////                Log.d("ITM", "adapter notified")
////
////            } catch (exception: Exception) {
////                Log.d("ITM", "Error getting documents: $exception")
////            }
////        }
////
////    }
////}
////
////
