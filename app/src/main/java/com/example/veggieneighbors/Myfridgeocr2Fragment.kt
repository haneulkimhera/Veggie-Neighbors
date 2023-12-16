import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.veggieneighbors.MyfridgeFragment
import com.example.veggieneighbors.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.Query


class Myfridgeocr2Fragment : Fragment() {
    private var ocrResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ocrResult = it.getString(OCR_RESULT_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myfridgeocr2, container, false)

        val textView: TextView = view.findViewById(R.id.text_view_ocr_result)
        textView.text = ocrResult

        val imageCancel: ImageView = view.findViewById(R.id.imagecancel)
        imageCancel.setOnClickListener {
            goToMyfridgeFragment()
        }

        val addFridgeButton: ImageView = view.findViewById(R.id.add_to_my_fridge)
        addFridgeButton.setOnClickListener {
            addFridgeButton.isClickable = true
            addFridgeButton.isFocusable = true
            ocrResult?.let { result ->
                addProductsToFridge(result)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFridgeItems()
    }

    private fun loadFridgeItems() {
        val db = Firebase.firestore
        db.collection("FridgeItems")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 여기서 각 문서를 처리하여 RecyclerView에 데이터 추가
                }
                // RecyclerView 어댑터에 데이터 변경을 알림
            }
            .addOnFailureListener { exception ->
                // 데이터 로드 실패 처리
            }
    }

    private var lastDocumentId = 3

    private fun addProductsToFridge(ocrResult: String) {
        val products = parseOcrResult(ocrResult)

        products.forEach { product ->
            // document ID 생성
            lastDocumentId += 1
            val documentId = "FridgeItem$lastDocumentId"

            // 이미지 URL 가져오기
            val imageUrl = getImageUrlForProduct(product["name"].toString(), lastDocumentId)

            // Firestore 문서에 이미지 URL 추가
            (product as MutableMap)["img"] = imageUrl

            saveProductToFridge(documentId, product) {
                // 저장 완료 시 처리
                goToMyfridgeFragment()
            }
        }
    }









    private fun parseOcrResult(ocrResult: String): List<Map<String, Any>> {
        val productPattern = "Product:\\s*(.+)".toRegex()
        val datePattern = "Date:\\s*(\\d{4}\\.\\d{2}\\.\\d{2})".toRegex()

        val commonDate = datePattern.find(ocrResult)?.groups?.get(1)?.value ?: return listOf()
        val bestBeforeDate = calculateBestBefore(commonDate)

        val lines = ocrResult.split("\n")
        val products = mutableListOf<Map<String, Any>>()

        lines.forEachIndexed { index, line -> // index 추가
            if (line.startsWith("Product:")) {
                val productName = line.substringAfter("Product:").trim()

                if (productName.isNotEmpty()) {
                    val imageUrl = getImageUrlForProduct(productName, index) // index를 제공

                    val productMap = mapOf(
                        "name" to productName,
                        "purchased date" to commonDate,
                        "Best before" to bestBeforeDate,
                        "img" to imageUrl
                    )
                    products.add(productMap)
                }
            }
        }

        return products
    }



    private fun getImageUrlForProduct(productName: String, index: Int): String {
        return "gs://veggie-neighbors-3df82.appspot.com/Fridge/index$index.jpg"
    }


    private fun saveProductToFridge(documentId: String, product: Map<String, Any>, onProductAdded: () -> Unit) {
        val db = Firebase.firestore
        val collectionRef = db.collection("Fridge Posts")
        val docRef = collectionRef.document(documentId)

        docRef.set(product)
            .addOnSuccessListener {
                onProductAdded()
            }
            .addOnFailureListener {
                // 실패 처리
            }
    }

    private fun calculateBestBefore(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val parsedDate = dateFormat.parse(date) ?: Date()
        calendar.time = parsedDate
        calendar.add(Calendar.WEEK_OF_YEAR, 2)
        return dateFormat.format(calendar.time)
    }



    private fun goToMyfridgeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, MyfridgeFragment())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val OCR_RESULT_KEY = "ocrResult"

        @JvmStatic
        fun newInstance(ocrResult: String) =
            Myfridgeocr2Fragment().apply {
                arguments = Bundle().apply {
                    putString(OCR_RESULT_KEY, ocrResult)
                }
            }
    }
}
