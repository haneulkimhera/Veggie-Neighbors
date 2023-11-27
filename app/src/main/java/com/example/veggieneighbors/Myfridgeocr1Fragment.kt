package com.example.veggieneighbors

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class Myfridgeocr1Fragment : Fragment() {
    private var imageUri: String? = null
    private lateinit var imageView: ImageView
    private lateinit var ocrStartImageView: ImageView
    private lateinit var textRecognizer: TextRecognizer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myfridgeocr1, container, false)
        imageView = view.findViewById(R.id.imageView_selected)
        ocrStartImageView = view.findViewById(R.id.imageView14)

        arguments?.getString(IMAGE_URI_KEY)?.let { uriString ->
            imageView.setImageURI(Uri.parse(uriString))
            imageUri = uriString
        }

        setupRecognizer()
        setupOcrStartImageView()

        return view
    }

    private fun setupRecognizer() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private fun setupOcrStartImageView() {
        ocrStartImageView.setOnClickListener { startOcr() }
    }

    private fun startOcr() {
        val uri = Uri.parse(imageUri)
        val inputImage = InputImage.fromFilePath(requireContext(), uri)
        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                // Process the result to extract the desired information
                val lines = visionText.text.split("\n")

                // Patterns to match the products and prices
                val productPattern = "Product:\\s*(.+)".toRegex()
                val pricePattern = "Price:\\s*(\\d+)".toRegex()
                val datePattern = "Date:\\s*(\\d{4}\\.\\d{2}\\.\\d{2})".toRegex()

                // Extract the date
                val date = datePattern.find(visionText.text)?.groups?.get(1)?.value

                // Lists to store products and prices
                val products = mutableListOf<String>()
                val prices = mutableListOf<String>()

                lines.forEach { line ->
                    when {
                        productPattern.containsMatchIn(line) -> {
                            productPattern.find(line)?.groups?.get(1)?.value?.trim()?.let {
                                products.add(it)
                            }
                        }
                        pricePattern.containsMatchIn(line) -> {
                            pricePattern.find(line)?.groups?.get(1)?.value?.trim()?.let {
                                prices.add(it)
                            }
                        }
                    }
                }

                // Build the result string with the extracted information
                val ocrData = StringBuilder()
                date?.let { ocrData.append("Date: $it\n") }

                val productPricePairs = products.zip(prices)
                productPricePairs.forEach { (product, price) ->
                    ocrData.append("Product: $product\nPrice: $price\n")
                }
                // Now pass the processed text to the next fragment
                openMyfridgeocr2FragmentWithResult(ocrData.toString())
            }
            .addOnFailureListener { e ->
            }
    }




    private fun openMyfridgeocr2FragmentWithResult(resultText: String) {
        val fragment = Myfridgeocr2Fragment.newInstance(resultText)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        private const val IMAGE_URI_KEY = "imageUri"

        @JvmStatic
        fun newInstance(imageUri: String) =
            Myfridgeocr1Fragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URI_KEY, imageUri)
                }
            }
    }




}
