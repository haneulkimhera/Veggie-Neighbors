    package com.example.veggieneighbors

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.media3.common.util.Log
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.veggieneighbors.databinding.FragmentHomeBinding
    import com.example.veggieneighbors.databinding.FragmentMyfridgeBinding
    import com.google.firebase.Firebase
    import com.google.firebase.firestore.firestore
    import com.google.firebase.storage.FirebaseStorage

    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"

    class MyfridgeFragment : Fragment() {
        private lateinit var binding: FragmentMyfridgeBinding
        private lateinit var productAdapter: FridgeAdapter
        private val productList = mutableListOf<FridgeItem>()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentMyfridgeBinding.inflate(inflater, container, false)

            setupRecyclerView()

            // Placeholder for fetching data from Firebase
            fetchDataFromFirestore()

            return binding.root
        }

        private fun setupRecyclerView() {
            productAdapter = FridgeAdapter(productList)
            binding.recyclerViewMyFridge.apply {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        private fun fetchDataFromFirestore() {
            val db = Firebase.firestore
            val storage = FirebaseStorage.getInstance()

            val fridgePostsRef = db.collection("Fridge Posts")
            fridgePostsRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val bestBefore = document.getString("Best before") ?: ""
                        val gsUrl = document.getString("img") ?: ""
                        val name = document.getString("name") ?: ""
                        val purchasedDate = document.getString("purchased date") ?: ""

                        // Firebase Storage 참조를 가져옵니다.
                        val storageRef = storage.getReferenceFromUrl(gsUrl)

                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            // 여기서 'uri.toString()'가 다운로드 URL입니다.
                            val fridgeItem = FridgeItem(bestBefore, uri.toString(), name, purchasedDate)
                            productList.add(fridgeItem)
                            productAdapter.notifyDataSetChanged() // 데이터가 업데이트 되었으니 알립니다.
                        }.addOnFailureListener {
                            // 에러 처리를 합니다.
                        }
                    }
                }
        }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Set click listener for imageView7
            binding.imageView7.setOnClickListener {
                navigateToMyfridgeocrFragment()
            }
        }

        private fun navigateToMyfridgeocrFragment() {
            val myfridgeocrFragment = MyfridgeocrFragment()

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fl_container, myfridgeocrFragment)
                addToBackStack(null)
                commit()
            }
        }



        companion object {

            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                ProductsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }


    }
