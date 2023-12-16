package com.example.veggieneighbors

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.veggieneighbors.databinding.FragmentMyfridgeocrBinding
import java.io.File
import java.io.FileOutputStream

class MyfridgeocrFragment : Fragment() {
    private lateinit var binding: FragmentMyfridgeocrBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyfridgeocrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView11.setOnClickListener {
            openCamera()
        }

        binding.imageView13.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            tryToOpenCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    private fun tryToOpenCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Log.e("MyfridgeocrFragment", "Camera not found", e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryToOpenCamera()
        } else {
            Log.e("MyfridgeocrFragment", "Camera permission denied")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val imageUri = data.data
                    imageUri?.let { navigateToMyfridgeocr1Fragment(it) }
                }
                CAMERA_REQUEST_CODE -> {
                    val imageUri = data.extras?.get("data") as? Bitmap
                    imageUri?.let { bitmap ->
                        // TODO: 여기서 카메라로 찍은 비트맵 이미지를 Uri로 변환하거나 다른 방식으로 처리

                        val uri = convertBitmapToUri(bitmap)
                        navigateToMyfridgeocr1Fragment(uri)
                    }
                }
            }
        }
    }
    private fun convertBitmapToUri(bitmap: Bitmap): Uri {
        // 임시 파일을 생성합니다.
        val file = File(requireContext().cacheDir, "temp_image" + System.currentTimeMillis() + ".png")
        file.createNewFile()

        // 파일에 비트맵을 저장합니다.
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // 파일의 Uri를 반환합니다.
        return Uri.fromFile(file)
    }


    // 이미지 갤러리에서 가져오기
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun navigateToMyfridgeocr1Fragment(imageUri: Uri) {
        val myfridgeocr1Fragment = Myfridgeocr1Fragment.newInstance(imageUri.toString())
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, myfridgeocr1Fragment)
            commit()
        }
    }




    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val CAMERA_REQUEST_CODE = 100
    }
}