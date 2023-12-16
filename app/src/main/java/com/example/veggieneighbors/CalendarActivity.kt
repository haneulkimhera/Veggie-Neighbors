package com.example.veggieneighbors

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var saveBtn: Button
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var diaryTextView: TextView
    private lateinit var diaryContent: TextView
    private lateinit var title: TextView
    private lateinit var contextEditText: EditText
    private var selectedDate: String = ""
    private var currentDocumentId: String? = null
    private lateinit var backBtn: ImageButton

    // Firebase Firestore 인스턴스
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // UI 컴포넌트 초기화
        initUI()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)

            diaryTextView.text = selectedDate
            checkDay(year, month + 1, dayOfMonth)
        }

        saveBtn.setOnClickListener {
            saveDiary()
        }

        updateBtn.setOnClickListener {
            updateDiary()
        }

        deleteBtn.setOnClickListener {
            deleteDiary()
        }

        backBtn.setOnClickListener {
            // "돌아가기" 버튼 클릭 시 이전 화면으로 이동
            onBackPressed()
        }
    }

    private fun initUI() {
        calendarView = findViewById(R.id.calendarView)
        saveBtn = findViewById(R.id.saveBtn)
        updateBtn = findViewById(R.id.updateBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        diaryTextView = findViewById(R.id.diaryTextView)
        diaryContent = findViewById(R.id.diaryContent)
        title = findViewById(R.id.title)
        contextEditText = findViewById(R.id.contextEditText)
        backBtn = findViewById(R.id.backButton) // "돌아가기" 버튼 초기화
        title.text = "Calendar"
    }

    private fun checkDay(year: Int, month: Int, day: Int) {
        val date = String.format("%d-%d-%d", year, month, day)
        db.collection("FridgePosts")
            .whereEqualTo("date", date)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // 일정이 없는 경우 입력 필드 활성화
                    contextEditText.isEnabled = true
                    resetUI()
                } else {
                    for (document in documents) {
                        currentDocumentId = document.id
                        val content = document.getString("content") ?: ""
                        contextEditText.setText(content)
                        diaryContent.text = content // 현재 일정 내용 표시
                        updateUIForExistingEvent()
                    }
                }
            }
            .addOnFailureListener {
                // Handle failure
                resetUI()
            }
    }

    private fun saveDiary() {
        val content = contextEditText.text.toString()
        val timestamp = FieldValue.serverTimestamp()
        val event = hashMapOf("date" to selectedDate, "content" to content, "timestamp" to timestamp)

        db.collection("FridgePosts").add(event)
            .addOnSuccessListener {
                currentDocumentId = it.id
                diaryContent.text = content // 저장된 일정 내용 표시
                updateUIForExistingEvent()
                contextEditText.isEnabled = false // 입력 필드 비활성화
            }
            .addOnFailureListener {
                // Handle failure
            }
    }


    private fun updateDiary() {
        val content = contextEditText.text.toString()
        currentDocumentId?.let {
            db.collection("FridgePosts").document(it)
                .update("content", content)
                .addOnSuccessListener {
                    diaryContent.text = content
                    contextEditText.isEnabled = true
                }
                .addOnFailureListener {
                    contextEditText.isEnabled = false
                }
        }
    }

    private fun deleteDiary() {
        currentDocumentId?.let {
            db.collection("FridgePosts").document(it)
                .delete()
                .addOnSuccessListener {
                    resetUI()
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    private fun resetUI() {
        contextEditText.setText("")
        diaryContent.text = ""
        updateBtn.visibility = View.INVISIBLE
        deleteBtn.visibility = View.INVISIBLE
        saveBtn.visibility = View.VISIBLE
    }

    private fun updateUIForExistingEvent() {
        updateBtn.visibility = View.VISIBLE
        deleteBtn.visibility = View.VISIBLE
        saveBtn.visibility = View.INVISIBLE
    }
}
